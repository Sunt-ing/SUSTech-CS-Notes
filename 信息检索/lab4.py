class Correction:
    """
    用途：当用户输入的当个词不存在于词库中时，返回词库中最接近的若干个词。使用Jaccard距离算法。
    作者：孙挺   时间：2020-03-14
    """
    def __init__(self):
        # 形式举例： inverted_index_3gram['sub']=set({2,7,333,88,33512})
        self.inv_idx_3g_str2set = dict()
        self.word_3gram_list = list()

    def calculate(self, item):
        """
        此时，word_3gram_list已经是有序的
        """
        item_3gram_list = sorted([item[i:i + 3] for i in range(0, len(item) - 3 + 1)])
        idx_item, idx_word, cnt = 0, 0, 0
        while idx_item < len(item_3gram_list) and idx_word < len(self.word_3gram_list):
            if item_3gram_list[idx_item] == self.word_3gram_list[idx_word]:
                cnt += 1
                idx_item += 1
                idx_word += 1
            elif item_3gram_list[idx_item] < self.word_3gram_list[idx_word]:
                idx_item += 1
            else:
                idx_word += 1
        return cnt / (len(item_3gram_list) + len(self.word_3gram_list) - cnt)

    def query(self, word):
        """
        集分词、算Jaccard系数、找系数最大值项于一体
        """
        candidates_set = set()
        for i in range(len(word) - 3 + 1):
            sub = word[i:i + 3]
            self.word_3gram_list.append(sub)

            if self.inv_idx_3g_str2set.get(sub):
                candidates_set.update(self.inv_idx_3g_str2set.get(sub))
        self.word_3gram_list.sort()

        max_coe = 0
        coe_item_list = [[self.calculate(item), item] for item in candidates_set]
        for i in coe_item_list:
            if i[0] > max_coe:
                max_coe = i[0]
        ret = []
        for i in coe_item_list:
            if i[0] == max_coe:
                ret.append(i[1])
        return ret

    def read_csv(self, path):
        file = open(path, mode='r+')
        lines = file.readlines()
        for line in lines:
            tokens = line.split(',')
            key = tokens[0]
            for i in range(len(key) - 3 + 1):
                sub_key = key[i:i + 3]
                if not self.inv_idx_3g_str2set.get(sub_key):
                    self.inv_idx_3g_str2set[sub_key] = set()
                self.inv_idx_3g_str2set[sub_key].add(key)


if __name__ == "__main__":
    correction = Correction()
    PATH = "./inverted_index_lab4.csv"
    correction.read_csv(PATH)

    query_terms = ['zood', 'feaft', 'sprdng', 'twisg', 'clat']
    query_answers = [['food', 'good', 'wood'], ['fear', 'leaf'], ['spring'], ['twist'], ['flat', 'late']]
    for idx, term in enumerate(query_terms):
        rtn_list = sorted(correction.query(term))
        query_answers[idx].sort()
        if not (rtn_list == query_answers[idx]):
            print('你返回的数组是', rtn_list)
            print('但答案是', query_answers[idx])
            raise ValueError("你算的是什么鬼？？？")
    print("Congratulations!")
