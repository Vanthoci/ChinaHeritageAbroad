import csv
import pandas as pd
output_file = 'C:\\Users\\15211\\Desktop\\数据\\final_data\\Guangdong_museum.csv'
input_file='C:\\Users\\15211\\Desktop\\数据\\data\\data\\Guangdong_museum.csv'
with open(output_file, 'w', newline='', encoding='utf-8') as f_out:
    count=24765
    writer = csv.writer(f_out)
    writer.writerow(["collectID","名称","年代","年代备注","类型","图片","详情","博物馆"])  # 写入表头
    with open(input_file, 'r', encoding='utf-8') as f_in:
        reader = csv.DictReader(f_in)
        for row in reader:
            count=count+1
            writer.writerow([count,row["名称"],row["年代"],row["年代备注"],row["类型"],row["图片"],row["详情"],"广东博物馆"])
            
print("finished!!!")