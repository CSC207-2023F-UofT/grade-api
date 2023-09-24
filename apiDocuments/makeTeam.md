
# Make a team

Make a team (note: everyone in the same team can see each other's grades)

**URL** : `/team`

**Method** : `POST`

**Auth required** : Required in header `Authorization`.

**Required Request Body**
```json
{
    "name": "The name of the team",
    "utorid": "The utorid"
}
```
## Success Responses

**Condition** : The team name has not be taken yet. And the utorid is not in a team yet.

**Code** : `200 OK`

**Content example** : 

```json
{
    "message": "Grade updated successfully",
    "status_code": 200
}
```

## Error Response

### Already in a team

**Condition** : The UTORID is already in a team.

**Code** : `400 BAD REQUEST`

**Content example** :

```json
{
    "message": "Team TAs created successfully",
    "status_code": 200
}
```

### Team name is not available
**Condition** : The team name is not available.

**Code** : `400 BAD REQUEST`

**Content example** :

```json
{
    "message": "Team already exists",
    "status_code": 400
}
```

### API Token is invalid

**Condition** : The given authorization token doesn't match with the ones that have the access to the utorid. Or the authorization token doesn't exist.
See the documentation for signUp for how to get a token.

**Code** : `401`

**Content example** :

```json
{
    "message": "Invalid token",
    "status_code": 401
}
```

### Server Error

**Condition** : The backend server has an issue.

**Code** : `500 Internal Server Error`

**Content example** :

```json
{
   "status_code": 500,
   "message": "Error making team"
}
```