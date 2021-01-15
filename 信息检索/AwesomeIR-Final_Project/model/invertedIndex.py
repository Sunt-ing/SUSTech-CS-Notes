import os, json, math
from util import stop
from nltk.tokenize import RegexpTokenizer

en_stops = set(stop.stopwords)
word_tokenizer = RegexpTokenizer('[A-Za-z]+')

W_LIKES, W_COMMENTS, W_SHARES = 1, 4, 8

# OLD_PATH = r"D:\Code_PyCharm\IR_Final_Project\data\originalData"
OLD_PATH = r"C:\Users\Administrator\Desktop\us-financial-news-articles\2018_01_112b52537b67659ad3609a234388c50a\aa"
OLD_PATH = r"C:\Users\Administrator\Desktop\us-financial-news-articles"
REPORT_LOAD_FREQUENCY = 10000  # 原本有30万个文件存储在5个文件夹中
MAX_PREVIEW_LEN = 500
N = 10
JACCARD_GRAM_NUM = 3


class InvertedIndex:
    def __init__(self, data_addr):
        global OLD_PATH
        OLD_PATH = data_addr
        self.file_num, self.chunk_count = 0, 0
        self.doc_to_len = dict()
        self.doc_to_file_path = dict()  # 新文档的位置
        self.token_to_doc_to_fre = dict()
        self.type_to_doc_to_value = dict()
        self.n_gram_to_tokens = dict()

        self.init_type_to_doc_to_value()
        self.build_token_to_doc_to_fre()
        self.calculate_lens()
        self.build_n_gram_to_token()

    def build_n_gram_to_token(self):
        for token in self.token_to_doc_to_fre:
            for i in range(len(token) - 3 + 1):
                n_gram = token[i:i + 3]
                if not self.n_gram_to_tokens.get(n_gram):
                    self.n_gram_to_tokens[n_gram] = set()
                self.n_gram_to_tokens[n_gram].add(token)

    def cos_search(self, query):
        query_set = set(query.lower().split())
        score = dict()
        for term in query_set:
            if term in self.token_to_doc_to_fre:
                df = self.file_num / len(self.token_to_doc_to_fre[term])
                for doc_id in self.token_to_doc_to_fre[term]:
                    token_w = (1 + math.log10(self.token_to_doc_to_fre[term][doc_id])) * math.log10(df)
                    query_w = (1 + math.log10(1)) * math.log10(df)
                    # token_w /= self.doc_lens[doc_id]
                    if doc_id in score:
                        score[doc_id] += token_w * query_w
                    else:
                        score[doc_id] = token_w * query_w
        for term in score:
            score[term] /= self.doc_to_len[term]
        return score

    def view(self, top_n_doc):
        if not top_n_doc:
            return "No result found."

        top_n_doc = top_n_doc[:N]
        print("top n doc是：\n", top_n_doc)
        top_n_url = [self.type_to_doc_to_value["url"][doc] for doc in top_n_doc]
        top_n_title = [self.type_to_doc_to_value["title"][doc] for doc in top_n_doc]
        top_n_preview = [self.type_to_doc_to_value["preview"][doc] for doc in top_n_doc]
        top_n_all = ""
        for i in range(min(len(top_n_doc), N)):
            top_n_all += str(i + 1) + ".\nTitle: " + top_n_title[i] + "\nPreview: " + top_n_preview[i] + "\n\nLink: " + \
                         top_n_url[i] + "\n\n\n"
        print("top 10 document for your get_most_possible_words: \n", top_n_all)
        return top_n_all.replace("\n", "\r\n")

    def top_n(self, query):
        result = self.cos_search(query)
        t = list(result.items())
        t.sort(key=lambda x: x[1], reverse=True)
        top_n_doc = [i[0] for i in t[:N]]
        return self.view(top_n_doc)

    def build_token_to_doc_to_fre(self):
        for root, dirs, files in os.walk(OLD_PATH, topdown=True):
            for file in files:
                if self.file_num % REPORT_LOAD_FREQUENCY == REPORT_LOAD_FREQUENCY - 1:
                    print("Have Loaded " + str(self.file_num) + ' pieces of news...')

                file_path = os.path.join(root, file)
                self.doc_to_file_path[self.file_num] = os.path.abspath(file_path)
                with open(file_path, "r+", encoding="UTF8") as f:
                    data = json.load(f)
                    self.spimi_invert(data, self.file_num)
                self.file_num += 1

    def calculate_lens(self):
        for term in self.token_to_doc_to_fre:
            # 计算tf
            # 时间换内存

            idf = math.log10(1 + (self.file_num + 1) / (1 + len(self.token_to_doc_to_fre[term])))
            for doc in self.token_to_doc_to_fre[term]:
                tf = 1 + math.log10(self.token_to_doc_to_fre[term][doc])
                temp = (tf * idf) ** 2
                if doc in self.doc_to_len:
                    self.doc_to_len[doc] += temp
                else:
                    self.doc_to_len[doc] = temp
        for term in self.doc_to_len:
            self.doc_to_len[term] = math.sqrt(self.doc_to_len[term])

    def init_type_to_doc_to_value(self):
        self.type_to_doc_to_value.clear()

        self.type_to_doc_to_value["shares"] = dict()
        self.type_to_doc_to_value["likes"] = dict()
        self.type_to_doc_to_value["comments"] = dict()
        self.type_to_doc_to_value["hot"] = dict()

        self.type_to_doc_to_value["url"] = dict()
        self.type_to_doc_to_value["published"] = dict()
        self.type_to_doc_to_value["site"] = dict()
        self.type_to_doc_to_value["title"] = dict()
        self.type_to_doc_to_value["preview"] = dict()

    def spimi_invert(self, data, doc_id):

        self.type_to_doc_to_value["url"][doc_id] = data["url"]
        self.type_to_doc_to_value["title"][doc_id] = data["title"]
        # 如： 2018-02-01
        self.type_to_doc_to_value["published"][doc_id] = data["published"][:10]
        self.type_to_doc_to_value["site"][doc_id] = data["thread"]["site"]
        self.type_to_doc_to_value["preview"][doc_id] = data["text"][:MAX_PREVIEW_LEN]

        self.type_to_doc_to_value["shares"][doc_id] = 0
        self.type_to_doc_to_value["likes"][doc_id] = 0
        self.type_to_doc_to_value["comments"][doc_id] = 0
        for media in data["thread"]["social"]:
            if "shares" in data["thread"]["social"][media]:
                self.type_to_doc_to_value["shares"][doc_id] += data["thread"]["social"][media]["shares"]
            if "likes" in data["thread"]["social"][media]:
                self.type_to_doc_to_value["likes"][doc_id] += data["thread"]["social"][media]["likes"]
            if "comments" in data["thread"]["social"][media]:
                self.type_to_doc_to_value["comments"][doc_id] += data["thread"]["social"][media]["comments"]
        self.type_to_doc_to_value["hot"][doc_id] = self.type_to_doc_to_value["shares"][doc_id] * W_SHARES \
                                                   + self.type_to_doc_to_value["likes"][doc_id] * W_LIKES \
                                                   + self.type_to_doc_to_value["comments"][doc_id] * W_COMMENTS

        tokens = word_tokenizer.tokenize(data["text"].lower())
        # print("Tokenize的结果是：", tokens)

        #  去除停词
        # tokens = [i for i in tokens if i not in en_stops]

        for token in tokens:
            if token in self.token_to_doc_to_fre:
                if doc_id in self.token_to_doc_to_fre[token]:
                    self.token_to_doc_to_fre[token][doc_id] += 1
                else:
                    self.token_to_doc_to_fre[token][doc_id] = 1
            else:
                self.token_to_doc_to_fre[token] = dict()
                self.token_to_doc_to_fre[token][doc_id] = 1
