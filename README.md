# Spring Academy - Security 




## Httpie Commands

**Getting a token**

```shell
export TOKEN=`http POST :8080/security/token -a sarah1:abc123 | awk '{print $1}'`
```

**GET cashcards**

```shell
http :8080/cashcards -A bearer -a $TOKEN
```

**POST a cashcard**

```shell
http POST :8080/cashcards amount=50.89 owner=sarah1 -A bearer -a $TOKEN 
```



