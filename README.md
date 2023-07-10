# Module 2 - Step 13 - Lab: Processing Failures


```shell
jwt encode --aud "https://cashcard.example.org" \
  --iss "https://issuer.example.org" \
  --alg RS256 \
  --exp=+3600S \
  --sub "sarah1" \
--payload scope="cashcard:read cashcard:write" \
--secret @src/main/resources/authz.pem
```

