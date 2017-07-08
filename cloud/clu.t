  $ amm $TESTDIR/clu.sc
  use --help for help
  $ amm $TESTDIR/clu.sc --help
  clu (cluster) 0.1
  Usage: clu [create] [options] <args>...
  * (glob)
    --dry-run                dry run
    -s, --stack <value>      stack name, default: 'cluster'
  Command: create <hosts>...
  create is a command
    <hosts>...* (glob)
    --help                   this text
  $ amm $TESTDIR/clu.sc create alfa beta gamma --dry-run
  stack: cluster
  creating: alfa,beta,gamma
  $ amm $TESTDIR/clu.sc create alfa beta gamma -s spark --dry-run
  stack: spark
  creating: alfa,beta,gamma
