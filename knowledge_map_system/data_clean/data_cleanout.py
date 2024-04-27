import pandas as pd
import numpy as np

df = pd.read_csv("E:\\vscode1\\LiaoNing_museum.csv")
# df.drop_duplicates(keep="first",inplace=True)

#辽宁博物馆用这个
# df.drop(columns="seq",inplace=True)
df=df.replace(np.NaN,"nan")
# df["年代"]=df["年代"].str.replace(r'清.*',"清",regex=True).replace(r'明.*',"明",regex=True).replace(r'西晋.*',"西晋",regex=True).replace(np.NaN,'nan').replace(r'南朝.*',"南朝",regex=True).replace(r'北宋.*',"北宋",regex=True).replace(r'^元(?![公]).*',"元",regex=True)
# df["年代"]=df["年代"].str.replace(r'唐代.*',"唐",regex=True).replace(r'商代.*',"商",regex=True).replace(r'三国时期.*',"三国",regex=True).replace(np.NaN,'nan').replace("不详",'nan')
# df["图片"]=df["图片"].replace(np.NaN,"nan")   
# df["author"]=df["author"].replace(np.NaN,"nan")
# df["类型"]=df["类型"].replace(np.NaN,'nan')
df["collectYear"]=df["collectYear"].replace(r'^金(?![元]).*',"金",regex=True).replace(r'商.*',"商",regex=True).replace(r'秦.*',"秦",regex=True)
df.to_csv("E:\\vscode1\\LiaoNing_museum.csv", index=False)