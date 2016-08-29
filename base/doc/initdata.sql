INSERT INTO song.user (userId,email,password,phone,state) VALUES 
('0001','',NULL,NULL,NULL)
;

INSERT INTO song.treenode (nodeId,nodeName,orderNum,parent_nodeId) VALUES 
(1,'根节点',1,NULL)
,(2,'一级节点1',1,1)
,(3,'一级节点2',2,1)
,(4,'二级节点1-1',1,2)
,(10,'二级节点1-2',2,2)
,(11,'二级节点1-3',3,2)
,(12,'二级节点1-4',4,2)
;