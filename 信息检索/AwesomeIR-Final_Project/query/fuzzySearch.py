from nltk.tokenize import RegexpTokenizer
from model.invertedIndex import JACCARD_GRAM_NUM


class FuzzySearch:
    """
    用途：当用户输入的当个词不存在于词库中时，返回词库中最接近的若干个词。使用 Jaccard 距离算法。
    举例：
        query_terms = ['zood', 'feaft', 'sprdng', 'twisg', 'clat']
        可能的返回结果： [['food', 'good', 'wood'], ['fear', 'leaf'], ['spring'], ['twist'], ['flat', 'late']]
    作者：孙挺
    时间：2020-03-14
    """

    def __init__(self, index_dao):
        # 形式举例： inverted_index_3gram['sub']=set({2,7,333,88,33512})
        self.index_dao = index_dao  # 是一个 InvertedIndex 对象
        self.n_gram_set = set()

    def calculate_jaccard_coe(self, item):
        item_3gram_set = set([item[i:i + 3] for i in range(0, len(item) - 3 + 1)])
        cross_set = self.n_gram_set & item_3gram_set
        union_set = self.n_gram_set | item_3gram_set
        return len(cross_set) / len(union_set)

    def is_wrong_query(self, query):
        # 测试一个查询语句是否是合理的
        word_tokenizer = RegexpTokenizer('[A-Za-z]+')
        tokens = word_tokenizer.tokenize(query.lower())
        for token in tokens:
            if token not in self.index_dao.token_to_doc_to_fre:
                return token

    def get_most_possible_words(self, wrong_word):
        """
        集分词、算Jaccard系数、找系数最大值项于一体
        """
        n_gram_set = set([wrong_word[i:i + JACCARD_GRAM_NUM] for i in range(len(wrong_word) - JACCARD_GRAM_NUM + 1)])
        candidates_set = set()
        for n_gram in n_gram_set:
            new_set = set()
            if n_gram in self.index_dao.n_gram_to_tokens:
                new_set = self.index_dao.n_gram_to_tokens.get(n_gram)
            candidates_set = candidates_set | new_set

        coe_token_list = [[self.calculate_jaccard_coe(token), token] for token in candidates_set]
        max_coefficient = max([-1] + [i[0] for i in coe_token_list])
        max_possible_words = list()
        for i in coe_token_list:
            if i[0] == max_coefficient:
                max_possible_words.append(i[1])
        tip = "Sorry, we don't find any result for \'" + wrong_word + "\'\nDo you mean: " + str(
            max_possible_words) + '?'
        return tip
