import csv
import pandas as pd
output_file = 'C:\\Users\\15211\\Desktop\\数据\\final_data\\indiana_museum.csv'
input_file='C:\\Users\\15211\\Desktop\\数据\\data\\data\\indiana_museum.csv'
with open(output_file, 'w', newline='', encoding='utf-8') as f_out:
    count=26845
    writer = csv.writer(f_out)
    writer.writerow(["collectID","name","author","relicTime","imageUrl","description","realUrl","museum"])  # 写入表头
    with open(input_file, 'r', encoding='utf-8') as f_in:
        reader = csv.DictReader(f_in)
        for row in reader:
            count=count+1
            writer.writerow([count,row["name"],
                             row["author"],row["relicTime"],row["imageUrl"],
                             row["description"],row["realUrl"],"印第安纳博物馆"])
            
print("finished!!!")