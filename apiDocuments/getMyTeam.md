

# Get my team

Return the team (team name + members) that you are in.

**URL** : `/team`

**Method** : `GET`

**Auth required** : Required in header `Authorization`.

## Success Responses

**Condition** : The username associated with this authorization token is in a team.

**Code** : `200 OK`

**Content example** :

```json
{
  "message": "Team members retrieved successfully",
  "status_code": 200,
  "team": {
    "_id": {
      "$oid": "650ba3ec3c1af072a1abb6c5"
    },
    "members": [
      "t1chenpa",
      "t2chenpa"
    ],
    "name": "Tes"
  }
}
```

## Error Response

### Not in any team.

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