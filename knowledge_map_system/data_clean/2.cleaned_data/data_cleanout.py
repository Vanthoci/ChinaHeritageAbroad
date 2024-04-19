import pandas as pd
import numpy as np

df = pd.read_csv("E:\\vscode1\\LiaoNing_museum.csv")
df.drop_duplicates(keep="first",inplace=True)

#辽宁博物馆用这个
# df.drop(columns="seq",inplace=True)

df["author"]=df["author"].replace('未找到','nan').replace('匿名','nan').replace('匿名，匿名','nan')

df["imageUrl"]=df["imageUrl"].replace('未找到','nan')
df.to_csv("E:\\vscode1\\LiaoNing_museum.csv", index=False)