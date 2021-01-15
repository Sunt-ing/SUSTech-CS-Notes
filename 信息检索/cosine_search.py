import os, sys, pickle, math, time, re, xlwt
from nltk.tokenize import RegexpTokenizer


def sum_dict(a, b):
    temp = dict()
    for key in a.keys() | b.keys():
        temp[key] = sum([d.get(key, 0) for d in (a, b)])
    return temp


def get_size(obj, seen=None):
    size = sys.getsizeof(obj)
    if seen is None:
        seen = set()
    obj_id = id(obj)
    if obj_id in seen:
        return 0

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
    global map_num2oldPath, file_cnt
    if not os.path.exists(PATH):
        print("Wrong input path")
        exit(-1)
    if not os.path.exists(PATH_NEW):
        os.makedirs(PATH_NEW)

    cnt = 0
    for root, dirs, files in os.walk(PATH, topdown=True):
        for file in files:
            if cnt == 0:
                cnt += 1
                continue
            old_file_path = os.path.join(root, file)
            new_file_path = os.path.join(PATH_NEW, str(cnt))
            map_num2oldPath[cnt] = os.path.abspath(old_file_path)
            with open(old_file_path, "r+", newline="", encoding="ISO-8859-1") as f:
                content = f.readlines()[:]
            with open(new_file_path, "w+", newline="", encoding="ISO-8859-1") as f:
                f.writelines(content)
            cnt += 1
    file_cnt = cnt - 1


class InvertedIndex:
    def __init__(self):
        self.map_token = dict()
        self.map_token_chunk = dict()
        self.map_doc2len = dict()
        self.chunk_cnt = 1

    def build(self):
        if not os.path.exists(PATH_CHUNKS):
            os.makedirs(PATH_CHUNKS)
        i = 1
        for root, dirs, files in os.walk(PATH_NEW, topdown=True):
            dirs.sort()
            for file in sorted(files):
                self.spimi_invert(os.path.join(PATH_NEW, str(i)), i)
                i += 1

        chunk_path = os.path.join(PATH_CHUNKS, str(self.chunk_cnt) + ".txt")
        with open(chunk_path, "wb") as f:
            pickle.dump(self.map_token_chunk, f)
        self.map_token_chunk.clear()
        self.spimi_merge()

    def spimi_invert(self, doc_path, doc_id):
        with open(doc_path, "r+", encoding="ISO-8859-1") as file:
            content = file.read().lower()

        for term in RegexpTokenizer('[A-Za-z]+').tokenize(content):
            if sys.getsizeof(self.map_token_chunk) > 1000000:
                chunk_path = os.path.join(PATH_CHUNKS, str(self.chunk_cnt) + ".txt")
                with open(chunk_path, "wb") as file:
                    pickle.dump(self.map_token_chunk, file)
                self.map_token_chunk.clear()
                self.chunk_cnt += 1
            if term in self.map_token_chunk:
                if doc_id in self.map_token_chunk[term]:
                    self.map_token_chunk[term][doc_id] += 1
                else:
                    self.map_token_chunk[term][doc_id] = 1
            else:
                self.map_token_chunk[term] = dict()
                self.map_token_chunk[term][doc_id] = 1

    def spimi_merge(self):
        self.map_token.clear()
        for i in range(self.chunk_cnt):
            chunk_path = os.path.join(PATH_CHUNKS, str(i + 1) + ".txt")
            with open(chunk_path, "rb") as file:
                self.map_token_chunk = pickle.loads(file.read())
            term_total = set(self.map_token_chunk.keys()).union(set(self.map_token.keys()))
            for term in term_total:
                if term in self.map_token_chunk and term in self.map_token:
                    self.map_token[term] = sum_dict(self.map_token[term], self.map_token_chunk[term])
                elif term in self.map_token_chunk:
                    self.map_token[term] = self.map_token_chunk[term]

    def get_len(self):
        global file_cnt
        self.map_doc2len = dict()

        for term in self.map_token:
            df = file_cnt / len(self.map_token[term])
            for doc_id in self.map_token[term]:
                temp = (1 + math.log10(self.map_token[term][doc_id])) * math.log10(df)
                if doc_id in self.map_doc2len:
                    self.map_doc2len[doc_id] += temp * temp
                else:
                    self.map_doc2len[doc_id] = temp * temp
        for term in self.map_doc2len:
            self.map_doc2len[term] = math.sqrt(self.map_doc2len[term])

    def cosine_search(self, query):
        global file_cnt
        query_cnt = dict()
        scores = dict()
        for term in query.lower().split(" "):
            if term in query_cnt:
                query_cnt[term] += 1
            else:
                query_cnt[term] = 1
        for term in query_cnt:
            if term in self.map_token:
                df = file_cnt / len(self.map_token[term])
                for doc_id in self.map_token[term]:
                    token_w = (1 + math.log10(self.map_token[term][doc_id])) * math.log10(df)
                    query_w = (1 + math.log10(query_cnt[term])) * math.log10(df)
                    if doc_id in scores:
                        scores[doc_id] += token_w * query_w
                    else:
                        scores[doc_id] = token_w * query_w
        for term in scores:
            scores[term] /= self.map_doc2len[term]
        return scores

if __name__ == "__main__":
    PATH = "D:/Code_PyCharm/IR/20_newsgroups"
    PATH_NEW = "D:/Code_PyCharm/IR/new_files"
    PATH_CHUNKS = "D:/Code_PyCharm/IR/chunks"
    map_num2oldPath = dict()
    file_cnt = 0
    print("IR System is starting, please wait for a minute...")
    rename()
    words = InvertedIndex()
    words.build()
    words.get_len()
    print("Finished.")
    while True:
        user_input = input("What do you wanna search?")
        top10 = list(words.cosine_search(user_input).items())
        top10.sort(key=lambda x: x[1], reverse=True)
        print("Result:")
        if top10:
            for doc_term in top10[:10]:
                print(map_num2oldPath[doc_term[0]])
        else:
            print('Sorry. Nothing found.')
