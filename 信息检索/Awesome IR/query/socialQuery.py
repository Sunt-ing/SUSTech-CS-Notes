from model.invertedIndex import N


class SocialQuery:
    def __init__(self, index_dao):
        self.index_dao = index_dao

    def query(self, query, query_kind):
        result = self.index_dao.cos_search(query)
        if not query or not result:
            return ""

        if query_kind == "2":
            kind = "likes"
        elif query_kind == "3":
            kind = "shares"
        elif query_kind == "4":
            kind = "comments"
        elif query_kind == "5":
            kind = "hot"
        else:
            return "WRONG Query Type."

        result = self.index_dao.cos_search(query)
        result = list(result.keys())

        kind_doc_list = [(doc, self.index_dao.type_to_doc_to_value[kind][doc]) for doc in result]
        kind_doc_list.sort(key=lambda x: x[1], reverse=True)
        top_n_doc = [i[0] for i in kind_doc_list[:N]]
        return self.index_dao.view(top_n_doc)
