# Note：同时包含lab2和lab3的内容
# 用到的知识点：
# A. 面向测试：1. 研究失败的例子为什么失败了
# 2. 对于每一种不熟悉的写法都要进行单元测试，不要想当然，如  a=b.sort()
# .sort() 是没有返回值的 ，但是python不会对此报错

# B. 面向对象：1. 对于一类功能，将之封装在一个类中
# 2. 函数的位置也要注意，相关的函数应该放在一起

# C. top-down：先给各个函数定义好接口，把对函数参数的要求记在注释中，然后再完成函数的主体部分
# 如果实现函数时有问题再重构，但一开始还是要有一个大概的框架的

import os, time, re, xlwt
from nltk.tokenize import RegexpTokenizer


class InvertedIdx:
    """
    Author: 孙挺
    Date: 2020-03-06
    Function: Build an inverted index model for information retrieval
    """

    def __init__(self):
        self.inverted_idx = dict()
        self.path2id = dict()
        self.num2doc = list(' ')

    def query_and(self, set1, set2):
        return set.intersection(set1, set2)

    def query_or(self, set1, set2):
        return set.union(set1, set2)

    def query_not(self, set1, set2):
        return set1.difference(set2)

    def add_news(self, folder):
        tmp = list()
        for root, dirs, files in os.walk(folder, topdown=True):
            for name in files:
                file_path = os.path.join(root, name)
                tmp.append(file_path)

        tmp.sort()
        for file_path in tmp:
            self.path2id[file_path] = len(self.num2doc)
            self.num2doc.append(file_path)

            file = open(file_path, mode='rb')
            rb_lines = file.readlines()
            file.close()

            str_lines = list()
            for x in rb_lines:
                # 2是指 b'
                # 3是指 \n'
                str_lines.append(str(x)[2:-3])
            document = '\n'.join(str_lines)

            rtval = re.search(r"Lines:\s+(\d+)", document)
            position = int(rtval.group(1))
            main_content = '\n'.join(str_lines[-position:])
            main_content = main_content.replace('\\t', ' ').replace('\\\'', ' ').replace('\\"', ' ')
            main_content = main_content.replace('\\b', ' ').replace('\\r', ' ')

            new_file_name = PATH_new + '/'
            new_file_name += str(self.path2id[file_path])
            new_file = open(new_file_name, mode='w')
            new_file.write(main_content)
            new_file.close()

            self.tokenize(main_content, self.path2id[file_path])

    def tokenize(self, body, file_id):
        body = body.lower()
        word_tokenizer = RegexpTokenizer('[A-Za-z]+')
        terms = word_tokenizer.tokenize(body)
        self.enlarge_set(terms, file_id)

    def enlarge_set(self, terms, file_id):
        for word in terms:
            if self.inverted_idx.get(word):
                self.inverted_idx[word][0] += 1
                self.inverted_idx[word][1].add(file_id)
            else:
                self.inverted_idx[word] = [1, {file_id}]

    def query(self, cmd):
        tokens, operators = self.resolve_cmd(cmd)
        answer_set = self.calculate(tokens, operators)
        answer_list = list(answer_set)
        answer_list.sort()
        return answer_list

    def resolve_cmd(self, cmd):
        # strip() 默认参数为 空格 或 换行符
        cmd = cmd.strip()
        tokens = list()
        operators = list()
        double_space = '  '
        single_space = ' '
        while double_space in cmd:
            cmd = cmd.replace(double_space, single_space)

        # 现在 cmd='a AND b'
        # print('NOW cmd is \"', cmd, "\"", end='')
        token_operator_list = cmd.split(single_space)
        operators_length = len(token_operator_list) // 2
        tokens.append(token_operator_list[0])
        for i in range(operators_length):
            operators.append(token_operator_list[2 * i + 1])
            tokens.append(token_operator_list[2 * i + 2])
        return tokens, operators

    def calculate(self, tokens, operators):
        # 传进来参数的是有大小写的
        if not tokens:
            print('NO TOKEN.')
            return None
        init_set = self.inverted_idx[tokens[0]][1]
        for idx, op in enumerate(operators):
            # 小写之
            couple_token = tokens[idx + 1].lower()
            couple_set = self.inverted_idx[couple_token][1]
            if op == 'AND':
                init_set = self.query_and(init_set, couple_set)
            elif op == 'OR':
                init_set = self.query_or(init_set, couple_set)
            elif op == 'NOT':
                init_set = self.query_not(init_set, couple_set)
            else:
                raise Exception("WRONG ARGUMENTS")
        return init_set

    def excel_inverted_index(self):
        # 单元格对齐
        alignment = xlwt.Alignment()

        # 水平对齐方式和垂直对齐方式
        alignment.horz = xlwt.Alignment.DIRECTION_LR
        alignment.vert = xlwt.Alignment.VERT_CENTER
        # 自动换行
        alignment.wrap = 1

        style = xlwt.XFStyle()
        style.alignment = alignment

        wb = xlwt.Workbook()
        ws = wb.add_sheet('all')

        ws.write(0, 0, 'key', style)
        ws.write(0, 1, 'frequency', style)
        ws.write(0, 2, 'postings', style)
        for i, key in enumerate(sorted(self.inverted_idx)):
            ws.write(i + 1, 0, key, style)
            ws.write(i + 1, 1, self.inverted_idx[key][0], style)
            tmp = list(self.inverted_idx[key][1])
            tmp.sort()
            ws.write(i + 1, 2, str(tmp), style)
        ws.col(0).width = 4000
        ws.col(1).width = 2200
        ws.col(2).width = 50000
        wb.save('./inverted_index.xls')

    def get_frequency(self, term):
        if not self.inverted_idx.get(term):
            return 0
        return self.inverted_idx[term][0]

    def get_postings(self, term):
        if not self.inverted_idx.get(term):
            return set()
        return self.inverted_idx[term][1]

    def get_inverted_index(self):
        return self.inverted_idx


if __name__ == '__main__':
    inverted = InvertedIdx()

    # 数据目录
    PATH = 'D:/Code_PyCharm/IR/20_newsgroups'
    # 生成的正文存储的目录
    PATH_new = 'D:/Code_PyCharm/IR/20_newsgroups_create'
    if not os.path.exists(PATH_new):
        os.makedirs(PATH_new)
    inverted.add_news(PATH)
    inverted.excel_inverted_index()

    # 测试样例
    # test_cases = ['cowboys', 'killer AND queen', 'risk AND rocket AND fuel',
    #               'abroad OR telephone OR wireless', 'labor NOT work NOT binding']
    # for cmd0 in test_cases:
    #     print(cmd0, ':', inverted.query(cmd0))

    # options:
    query_term = 'rocket'
    # print('the frequency of the term is:', inverted.get_frequency(query_term))
    # print('the postings of the term is:', inverted.get_postings(query_term))
    # print('Now the inverted index is:', inverted.get_inverted_index())
