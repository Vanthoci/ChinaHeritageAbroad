import csv
import pandas as pd

output_file = 'C:\\Users\\15211\\Desktop\\数据\\final_data\\add_id01.csv'
input_file='C:\\Users\\15211\\Desktop\\数据\\data\\data\\3D_LiaoNing_museum.csv'
with open(output_file, 'w', newline='', encoding='utf-8') as f_out:
    count=0
    writer = csv.writer(f_out)
    writer.writerow(["collectID","collectName","collectYear","collectDetailYear",
                     "collectType","collectLevel","collectTexture","detailSize",
                     "unearthedLocation","collectSynopsis","threeDimensionalInfo","collectImages","museum"])  # 写入表头
    with open(input_file, 'r', encoding='utf-8') as f_in:
        reader = csv.DictReader(f_in)
        for row in reader:
            count=count+1
            writer.writerow([count,row["collectName"],row["collectYear"],row["collectDetailYear"],
                     row["collectType"],row["collectLevel"],row["collectTexture"],row["detailSize"],
                     row["unearthedLocation"],row["collectSynopsis"],row["threeDimensionalInfo"],row["collectImages"],"辽宁博物馆"])
            
print("finished!!!")