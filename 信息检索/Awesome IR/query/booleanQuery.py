class BooleanQuery:

    def __init__(self, index_dao):
        self.index_dao = dict()
        self.index_dao = index_dao

    def check_args(self, cmd):
        if (not cmd) or len(cmd) == 0:
            return False
        cmd_tokens = cmd.split(" ")
        if len(cmd_tokens) % 2 == 0:
            return False

        for i in range(1, len(cmd_tokens), 2):
            if cmd_tokens[i].upper() not in {"AND", "OR", "NOT"}:
                return False
        # if cmd_tokens[0] not in self.index_dao.token_to_doc_to_fre:
        #     return False
        return True

    def clean_args(self, cmd):
        cmd = cmd.strip()
        double_space = '  '
        single_space = ' '
        while double_space in cmd:
            cmd = cmd.replace(double_space, single_space)
        # 现在 cmd='a AND b'
        return cmd

    def resolve_cmd(self, cmd):
        cmd_tokens = cmd.split(" ")
        tokens = [cmd_tokens[i] for i in range(0, len(cmd_tokens), 2)]
        operators = [cmd_tokens[i] for i in range(1, len(cmd_tokens), 2)]
        return tokens, operators

    def query(self, cmd):
        print("cmd是：", cmd)
        cmd = self.clean_args(cmd)
        if not self.check_args(cmd):
            print("参数审核不通过")
            return "Wrong argument format."
        tokens, operators = self.resolve_cmd(cmd)
        print("tokens, operators是：", tokens, operators)
        answer_set = self.calculate(tokens, operators)
        print("calculate finished.")
        answer_list = list(answer_set)
        return self.index_dao.view(answer_list)

    def calculate(self, tokens, operators):
        # 传进来参数的是有大小写的
        element_sets = []
        for token in tokens:
            token = token.lower()
            element_set = set()
            if token in self.index_dao.token_to_doc_to_fre:
                element_set = set(self.index_dao.token_to_doc_to_fre[token].keys())
            element_sets.append(element_set)

        init_set = element_sets[0]
        for idx, op in enumerate(operators):
            element_set = element_sets[idx + 1]
            op = op.upper()

            if op == 'AND':
                print("and前：", init_set)
                init_set = init_set & element_set
                print("and后：", init_set)
            elif op == 'OR':
                print("or前：", init_set)
                init_set = init_set | element_set
                print("or后：", init_set)
            elif op == 'NOT':
                print("not前：", init_set)
                init_set = init_set - element_set
                print("not后：", init_set)
            else:
                print("这个异常的操作符是：", op)
                raise Exception("WRONG ARGUMENTS")
        return init_set
