#!/bin/bash
(sleep 1
echo "*** Starting DFS"
/usr/hadoop/sbin/start-dfs.sh
echo "*** Starting YARN"
/usr/hadoop/sbin/start-yarn.sh
#echo "*** Starting HistoryServer"
#/usr/hadoop/sbin/mr-jobhistory-daemon.sh start historyserver
)&
/usr/sbin/sshd -D
