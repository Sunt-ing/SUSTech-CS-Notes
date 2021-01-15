s='aaa'
f=open('sunt.txt',mode='r',encoding='utf-8')
s=str(f)
print(s)
s=f.read()
fre_dict = {}
for word in s:
    if fre_dict.get(word):
        fre_dict[word]+=1
    else:
        fre_dict[word]=1
print(fre_dict)
f.close()