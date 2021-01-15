import os, sys
import pickle
from functools import reduce
from nltk.tokenize import RegexpTokenizer
import math

path_dic = dict()
file_num = 0

OLD_PATH = "20_newsgroups"
NEW_PATH = "news_groups"
CHUNK_PATH = "chunk_folder"


def sum_dict(a, b):
    temp = dict()
    for key in a.keys() | b.keys():
        temp[key] = sum([d.get(key, 0) for d in (a, b)])
    return temp


def get_size(obj, seen=None):
    """Recursively finds size of objects"""
    # 这段程序会严重拖慢处理速度
    size = sys.getsizeof(obj)
    if seen is None:
        seen = set()
    obj_id = id(obj)
    if obj_id in seen:
        return 0
    # Important mark as seen *before* entering recursion to gracefully handle
    # self-referential objects
    seen.add(obj_id)
    if isinstance(obj, dict):
        size += sum([get_size(v, seen) for v in obj.values()])
        size += sum([get_size(k, seen) for k in obj.keys()])
    elif hasattr(obj, '__dict__'):
        size += get_size(obj.__dict__, seen)
    elif hasattr(obj, '__iter__') and not isinstance(obj, (str, bytes, bytearray)):
        size += sum([get_size(i, seen) for i in obj])
    return size


def rename():
    global path_dic, file_num
    if not os.path.exists(OLD_PATH):
        print("no input folder found")
        exit(-1)
    if not os.path.exists(NEW_PATH):
        os.makedirs(NEW_PATH)

    i = 0
    for root, dirs, files in os.walk(OLD_PATH, topdown=True):
        dirs.sort()
        for file in sorted(files):
            if i == 0:
                i += 1
                continue
            old_file_path = os.path.join(root, file)
            new_file_path = os.path.join(NEW_PATH, str(i))
            # print(os.path.abspath(old_file_path))
            path_dic[i] = os.path.abspath(old_file_path)
            with open(old_file_path, "r+", newline="", encoding="ISO-8859-1") as f:
                content = f.readlines()[:]
            with open(new_file_path, "w+", newline="", encoding="ISO-8859-1") as f:
                f.writelines(content)
            i += 1
    file_num = i - 1
    print("rename done", file_num)


class InvertedList:

    def __init__(self):
        self.token_dic = dict()
        self.token_chunk = dict()
        self.chunk_count = 1
        self.doc_lens = dict()  # 用来归一化向量

    def build_token_dic(self):
        if not os.path.exists(CHUNK_PATH):
            os.makedirs(CHUNK_PATH)
        i = 1
        for root, dirs, files in os.walk(NEW_PATH, topdown=True):
            dirs.sort()
            for file in sorted(files):
                self.spimi_invert(os.path.join(NEW_PATH, str(i)), i)
                i += 1

        chunk_path = os.path.join(CHUNK_PATH, str(self.chunk_count) + ".txt")
        with open(chunk_path, "wb") as f:
            pickle.dump(self.token_chunk, f)
        self.token_chunk.clear()
        self.spimi_merge()

    def spimi_invert(self, doc_path, doc_id):
        with open(doc_path, "r+", encoding="ISO-8859-1") as f:
            content = f.read().lower()
        word_tokenizer = RegexpTokenizer('[A-Za-z]+')
        terms = word_tokenizer.tokenize(content)

        for term in terms:
            if sys.getsizeof(self.token_chunk) > 1000000:
                chunk_path = os.path.join(CHUNK_PATH, str(self.chunk_count) + ".txt")
                with open(chunk_path, "wb") as f:
                    pickle.dump(self.token_chunk, f)
                self.token_chunk.clear()
                self.chunk_count += 1
                print('create inverted list chunk: ', self.chunk_count)
            if term in self.token_chunk:
                if doc_id in self.token_chunk[term]:
                    self.token_chunk[term][doc_id] += 1
                else:
                    self.token_chunk[term][doc_id] = 1
            else:
                self.token_chunk[term] = dict()
                self.token_chunk[term][doc_id] = 1

    def spimi_merge(self):
        self.token_dic.clear()
        for i in range(self.chunk_count):
            chunk_path = os.path.join(CHUNK_PATH, str(i + 1) + ".txt")
            with open(chunk_path, "rb") as f:
                self.token_chunk = pickle.loads(f.read())
            term_total = set(self.token_chunk.keys()).union(set(self.token_dic.keys()))
            # print(self.token_dic.keys())
            for term in term_total:
                # print(term)
                if term in self.token_chunk and term in self.token_dic:
                    self.token_dic[term] = sum_dict(self.token_dic[term], self.token_chunk[term])
                elif term in self.token_chunk:
                    self.token_dic[term] = self.token_chunk[term]
            print("merge chunk " + str(i))

    def calculate_lens(self):
        global file_num
        self.doc_lens = dict()
        for term in self.token_dic:

            df = file_num / len(self.token_dic[term])
            for doc_id in self.token_dic[term]:
                temp = (1 + math.log10(self.token_dic[term][doc_id])) * math.log10(df)
                if doc_id in self.doc_lens:
                    self.doc_lens[doc_id] += temp * temp
                else:
                    self.doc_lens[doc_id] = temp * temp
        for term in self.doc_lens:
            self.doc_lens[term] = math.sqrt(self.doc_lens[term])
        print("file vector length done")

    def cos_search(self, query):
        global file_num
        query_list = query.lower().split()
        query_dic = dict()
        score = dict()
        for term in query_list:
            if term in query_dic:
                query_dic[term] += 1
            else:
                query_dic[term] = 1
        # print(query_dic)
        for term in query_dic:
            if term in self.token_dic:
                df = file_num / len(self.token_dic[term])
                for doc_id in self.token_dic[term]:
                    token_w = (1 + math.log10(self.token_dic[term][doc_id])) * math.log10(df)
                    query_w = (1 + math.log10(query_dic[term])) * math.log10(df)
                    # token_w /= self.doc_lens[doc_id]
                    if doc_id in score:
                        score[doc_id] += token_w * query_w
                    else:
                        score[doc_id] = token_w * query_w
        for term in score:
            score[term] /= self.doc_lens[term]
        return score


rename()
tokens = InvertedList()
tokens.build_token_dic()
tokens.calculate_lens()

while (1):
    query = input("Please input a query, input q for quit: ")
    if query.lower() == 'q':
        print('bye')
        exit(0)

    result = tokens.cos_search(query)
    t = list(result.items())
    t.sort(key=lambda x: x[1], reverse=True)
    top10 = t
    print("top 10 docment for your query: ")
    for doc_term in top10[:10]:
        print(path_dic[doc_term[0]])
