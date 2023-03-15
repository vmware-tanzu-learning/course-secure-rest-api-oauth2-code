#!/bin/bash

jwt encode --aud "https://cashcard.example.org" \
	--iss "https://issuer.example.org" \
	--alg RS256 \
	--exp=+3600S \
        --sub "sarah1" \
	-P "scp=[ \"cashcard:read\", \"cashcard:write\" ]" \
	--secret .$1
