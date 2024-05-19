import csv
import pandas as pd
output_file = 'C:\\Users\\15211\\Desktop\\数据\\final_data\\LiaoNing_museum.csv'
input_file='C:\\Users\\15211\\Desktop\\数据\\data\\data\\LiaoNing_museum.csv'
with open(output_file, 'w', newline='', encoding='utf-8') as f_out:
    count=33
    writer = csv.writer(f_out)
    writer.writerow(["collectID","collectName","collectYear",
                     "collectType","collectLevel","collectTexture",
                     "detailSize","num","source","museum"])  # 写入表头
    with open(input_file, 'r', encoding='utf-8') as f_in:
        reader = csv.DictReader(f_in)
        for row in reader:
            count=count+1
            writer.writerow([count,row["collectName"],row["collectYear"],
                     row["collectType"],row["collectLevel"],row["collectTexture"],
                     row["detailSize"],row["num"],row["source"],"辽宁博物馆"])
            
print("finished!!!")