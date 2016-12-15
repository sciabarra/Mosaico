if test -z "$1"
then echo "run a command in all - usage: $0 command" ; exit 1
fi
docker ps | awk '{print $1, $12}' | while read a b  
do echo '*** '$b' ***' 
docker exec $a "$@" 
done
