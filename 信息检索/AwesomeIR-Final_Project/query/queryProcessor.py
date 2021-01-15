from . import socialQuery, booleanQuery, videoQuery, fuzzySearch


class QueryProcessor:
    def __init__(self, index_dao):
        self.index_dao = index_dao
        self.fuzzy_search = fuzzySearch.FuzzySearch(self.index_dao)

    def query(self, message):
        query_type = message[0]
        query = message[1:]

        if query_type == '0':  # basic get_most_possible_words
            wrong_word = self.fuzzy_search.is_wrong_query(query)
            print("wrong word 是：", wrong_word)
            if wrong_word:
                print("所以word是有问题的：", wrong_word)
                result = self.fuzzy_search.get_most_possible_words(wrong_word)
                print("我们要返回的参考结果是：", result)
                return result
            return self.index_dao.top_n(query)

        elif query_type == '1':  # boolean get_most_possible_words
            result = booleanQuery.BooleanQuery(self.index_dao).query(query)
            print("返回的结果是：", result)
            return result

        elif query_type in {'2', '3', '4', '5'}:  # social get_most_possible_words
            wrong_word = self.fuzzy_search.is_wrong_query(query)
            if wrong_word:
                return self.fuzzy_search.get_most_possible_words(wrong_word)
            return socialQuery.SocialQuery(self.index_dao).query(query, query_type)

        elif query_type == '6':  # video get_most_possible_words
            wrong_word = self.fuzzy_search.is_wrong_query(query)
            if wrong_word:
                return self.fuzzy_search.get_most_possible_words(wrong_word)
            return videoQuery.VideoQuery(self.index_dao).query(query)

        else:
            error_message = "WRONG Query Type."
            print(error_message)
            return error_message
