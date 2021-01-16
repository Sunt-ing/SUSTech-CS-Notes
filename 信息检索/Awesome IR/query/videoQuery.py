from model.invertedIndex import N


class VideoQuery:
    def __init__(self, index_dao):
        self.index_dao = index_dao

    def query(self, query):
        result = self.index_dao.cos_search(query)
        result = list(result.keys())

        kind_doc_list = [(doc, self.index_dao.type_to_doc_to_value["url"][doc]) for doc in result if
                         "/video/" in self.index_dao.type_to_doc_to_value["url"][doc]]
        print("这个video query的kind_doc_list是", kind_doc_list)
        kind_doc_list.sort(key=lambda x: x[1], reverse=True)
        top_n_doc = [i[0] for i in kind_doc_list[:N]]
        if not top_n_doc:
            return "Sorry, no video found."
        return self.index_dao.view(top_n_doc)
