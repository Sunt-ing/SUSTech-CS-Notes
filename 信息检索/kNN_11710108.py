def calculate_distance(vec1, vec2):
    #  计算两个向量之间的欧几里得距离
    ret = 0
    for idx, component in enumerate(vec1):
        ret += (component - vec2[idx]) ** 2
    return ret ** 0.5


def calculate_prediction(score_doc_pairs, train_target):
    # 根据于原文档最接近的k个文档来推断原文档的类型

    # 各种类型的得分
    score_type_pairs = [[0, 1], [0, 2], [0, 3]]
    for i in score_doc_pairs:
        train_doc = i[1]
        train_type = train_target[train_doc]
        score_type_pairs[train_type - 1][0] += 1
    # 返回分数最高的文档
    return sorted(score_type_pairs)[-1][1]


def kNN_11710108(train_data, test_data, train_target, test_target, k=5):
    # 返回于test_data中每一个文档最可能的类型
    test_prediction = list()

    for doc_test in test_data:
        scores = list()
        for idx, doc_train in enumerate(train_data):
            score = calculate_distance(doc_test, doc_train)
            scores.append((score, idx))
        score_doc_pairs = sorted(scores)[0:k]
        # print("doc: ", score_doc_pairs)
        test_prediction.append(calculate_prediction(score_doc_pairs, train_target))
    return test_prediction


# train_data = [[0.43, 0.54], [0.143, 3.15], [0.43, 0.59], [0.2, 0.3], [0.1, 0.1], [0.43, 0.9], [0.2, 0.39]]
# test_data = [[0.43, 0.54], [0.01, 0.12], [0.43, 0.15]]
# train_target = [2, 1, 3, 1, 1, 3, 2]
# #  记得改回k值
# print(kNN_11710108(train_data, test_data, train_target, []))
