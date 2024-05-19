load csv from'file:///output_unique_author.csv' as line create(:藏品作者{author_name:line[0]});

load csv from'file:///output_unique_collectType.csv' as line create(:藏品类型{collectType:line[0]});

load csv from'file:///output_unique_source.csv' as line create(:藏品来源{source:line[0]});

load csv from'file:///output_unique_collectTexture.csv' as line create(:藏品材质{Texture:line[0]});

load csv from'file:///output_unique_collectLevel.csv' as line create(:藏品等级{Level:line[0]});

load csv from'file:///output_unique_collectYear.csv' as line create(:藏品年代{Year:line[0]});

load csv from'file:///output_unique_museum.csv' as line create(:藏品博物馆{museum:line[0]});

load csv from'file:///output_collection.csv' as line create(:藏品{num:line[0],名字:line[1]});

load csv from'file:///triple_author.csv' as line create(:作者{Subject:line[0],object:line[2],relation:line[1]});

match(n:藏品),(m:作者),(s:藏品作者) where n.num=m.Subject and s.author_name=m.object create(n)-[r:创作者为{relation:m.relation}]->(s);

load csv from'file:///triple_collectYear.csv' as line create(:年代{Subject:line[0],object:line[2],relation:line[1]});

match(n:藏品),(m:年代),(s:藏品年代) where n.num=m.Subject and s.Year=m.object create(n)-[r:处于的年代{relation:m.relation}]->(s);

load csv from'file:///triple_collectType.csv' as line create(:类型{Subject:line[0],object:line[2],relation:line[1]});

match(n:藏品),(m:类型),(s:藏品类型) where n.num=m.Subject and s.collectType=m.object create(n)-[r:类型为{relation:m.relation}]->(s);

load csv from'file:///triple_source.csv' as line create(:来源{Subject:line[0],object:line[2],relation:line[1]});

match(n:藏品),(m:来源),(s:藏品来源) where n.num=m.Subject and s.source=m.object create(n)-[r:来源于{relation:m.relation}]->(s);

load csv from'file:///triple_collectLevel.csv' as line create(:等级{Subject:line[0],object:line[2],relation:line[1]});

match(n:藏品),(m:等级),(s:藏品等级) where n.num=m.Subject and s.Level=m.object create(n)-[r:等级为{relation:m.relation}]->(s);

load csv from'file:///triple_collectTexture.csv' as line create(:材质{Subject:line[0],object:line[2],relation:line[1]});

match(n:藏品),(m:材质),(s:藏品材质) where n.num=m.Subject and s.Texture=m.object create(n)-[r:材质为{relation:m.relation}]->(s);

load csv from'file:///triple_museum.csv' as line create(:博物馆{Subject:line[0],object:line[2],relation:line[1]});

match(n:藏品),(m:博物馆),(s:藏品博物馆) where n.num=m.Subject and s.museum=m.object create(n)-[r:位于{relation:m.relation}]->(s);



MATCH p=()-->()
WITH p, rand() AS random
RETURN p
ORDER BY random
LIMIT 1000
