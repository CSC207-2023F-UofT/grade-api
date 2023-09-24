# Get a grade

Get a grade for a course for a specific UTORid. One can only access grades for themselves or the others in the same team.

**URL** : `/grade`

**Method** : `GET`

**Auth required** : Required in header `Authorization`.

**Required Request Parameters**
```json
{
    "utorid": "The utorid",
    "course": "The course code"
}
```
## Success Responses

**Condition** : Access to the utorid's grades is verified by the authorization token, and a grade for the course has been logged before.

**Code** : `200 OK`

**Content example** :

```json
{
  "grade": {
    "_id": {
      "$oid": "64b85b05e66b09ca82769e67"
    },
    "course": "CSC207",
    "grade": 85,
    "utorid": "t1chenpa"
  },
  "message": "Grade retrieved successfully",
  "status_code": 200,
  "utorid": "t1chenpa"
}
```

## Error Response

### Grade not found.

**Condition** : The grade of this course for this given utorid doesn't exist.

**Code** : `404 NOT FOUND`

**Content example** :

```json
{
    "message": "Grade not found",
    "status_code": 404
}
```

### API Token is invalid

**Condition** : The given authorization token doesn't match with the ones that have the access to the utorid.
Or the authorization token doesn't exist. See the documentation for signUp for how to get a token.

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
   "message": "Error retrieving grade"
}
```