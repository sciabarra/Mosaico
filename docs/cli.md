# Starting the command line shell

You can use commands in 3 ways

## Using commands from `sbt`

You can start `sbt` then run `amm` commands from the command line.

For example:

```
$ sbt
> amm ssh --stack spark --host master hostname -f
```

## From the `amm` command line

You can enter in the `amm` cli then run commands from there.

For example:

```
$ sbt
> amm
@ ssh("spark", "master", "hostname -f")
```

## Without sbt

If you have already installed [Ammonite](http://ammonite.io)
you can `cd cloud` and execute directly `amm`.

So you can do just the following:

```
$ amm aws.sc
$ amm aws.sc ssh --stack spark --host master hostname -f
$ amm
@ import $exec.aws
@ ssh("spark", "master", "hostname -f")
```

Note scripts are tested against version `0.9.9`.
To be sure of the version you may do better to start it from `sbt`.

# Defaults

Defaults are saved when you use them.

You can use:

```
amm ssh --stack spark --host hostname -f
```

Then the next time you execute the command you can just do

```
amm ssh hostname -f
```

The command `defaults` prints and let you set the defaults without executing any command.
