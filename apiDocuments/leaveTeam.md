# Record a grade

Rcord a grade of a course for a utorid.

**URL** : `/leaveTeam`

**Method** : `PUT`

**Auth required** : Required in header `Authorization`.

## Success Responses

**Condition** : Access to the utorid is verified by the authorization token, and a grade for the course has not been logged before.

**Code** : `200 OK`

**Content example** : 

```json
{
    "message": "You have left the team",
    "status_code": 200
}
```

## Error Response

### Grade already exists.

**Condition** : Not in a team.

**Code** : `404 NOT FOUND`

**Content example** :

```json
{
    "message": "You are not in a team",
    "status_code": 404
}
```

### API Token is invalid

**Condition** : The given authorization token doesn't match with the ones that have the access to the utorid. Or the authorization token doesn't exist. Students need to log in their teach lab account to see the token at https://wwwcgi.teach.cs.toronto.edu/~csc207h/cgi-bin/fall/test/test-cgi.
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