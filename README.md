# Spring Academy OAuth 2.0 REST API Security Course

This repository represents the structure of the [real-world simulated project](https://github.com/vmware-tanzu-learning/spring-academy/blob/main/docs/lab-authoring-style-guide.md#remember-youre-building-a-real-application) used to teach Spring Academy learners concepts taught in a course. 

This repo is a companion to the [Spring Academy OAuth 2.0 REST API Security Course repository](https://github.com/vmware-tanzu-learning/course-secure-rest-api-oauth2).

## How to use

- [ ] Duplicate this repository and use it as a basis or reference for your own course code repository.
- [ ] Set the visibility of this repo to Public
- [ ] Make sure `scripts/stage-codebase.sh` has executable permissions
- [ ] Create and push a branch named `prod`

### Branches and environments 

- `main` - Commits to this branch will automatically be deployed to Spring Academy **Staging**
- `prod` - Commits will be used for Spring Academy. Deploys are a manual process at this time. 

## Documentation

Learn more about building courses and labs in the [Spring Academy Contribution Guide](https://github.com/vmware-tanzu-learning/spring-academy/blob/main/CONTRIBUTING.md).

## HTTPie Commands

### GET All Cash Cards

```shell
http :8080/cashcards -A bearer -a $TOKEN
```

### POST a cashcard

```shell
http POST :8080/cashcards amount=50.89 owner=sarah1 -A bearer -a $TOKEN 
```
