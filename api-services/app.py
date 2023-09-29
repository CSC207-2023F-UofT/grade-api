from flask import Flask, make_response
from flask import jsonify
from pymongo import MongoClient
from pymongo.errors import PyMongoError
from flask import request, abort
from config import MONGO_DB_CONNECTION_STRING
import json
from bson import json_util
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address

client = MongoClient(MONGO_DB_CONNECTION_STRING)
db = client['grade-logging-api']
GRADE = db['grade']
TOKEN = db['token']
TEAM = db['team']

app = Flask(__name__)

# implement rate limit.
def get_client_key():
    # check if it is signUp or not. If it is, use get_remote_address instead.

    client_key = None

    if request.endpoint in ['signUp', 'get_all_teams', 'getAlldata']: 
        client_key = 'global'
    else: 
        client_key = request.headers.get('Authorization')

    print(client_key)

    return client_key

# limiter = Limiter(app, key_func=get_client_key)

limiter = Limiter(get_client_key, app=app)

@app.errorhandler(429)
def ratelimit_handler(e):
    return {
        "status_code": 429,
        "message": "Too many requests! Please check your code to make sure you are not sending more than 50 requests per minutes."
    }, 429


def api_key_middleware():
    if request.endpoint in ['signUp', 'get_all_teams', 'getAlldata']: return # for these two endpoints, no need to check the token.
    # for these endpoints, just need to check if the 
    authorization_header = request.headers.get("Authorization")

    print(authorization_header)

    if not authorization_header:
        return {
            "status_code": 401,
            "message": "Authorization header is required"
        }, 401

    the_doc = TOKEN.find_one({
        "token": authorization_header
    })
    if not the_doc:
        return {
            "status_code": 401,
            "message": "Invalid token"
        }, 401

    myUtorid = the_doc['utorid']

    if request.endpoint in ['get_grade']:
        # check if the myUtorid is the same as the utorid in the request. (by checking if the token provided matches the utorid)

        # need to check if in the same team.
        # get myUtorid by token.
        if request.method == 'GET':
            utorid = request.args.get('utorid') if 'utorid' in request.args else None
        else:
            utorid = request.json['utorid'] if 'utorid' in request.json else None
            

        # check if same team. Need members contains both utorid and myUtorid.
        the_doc = TEAM.find_one({
            "members": {"$in": [myUtorid]}
        })

        token_this_utorid_doc = TOKEN.find_one({"utorid": utorid})

        if not token_this_utorid_doc:
            return {
                "status_code": 401,
                "message": "The UTORid is not associated with a token. Please sign up first."
            }, 401

        token_this_utorid = token_this_utorid_doc['token']



        # check if the utorid is in the team.

        if (token_this_utorid != authorization_header) and (not the_doc or utorid not in the_doc['members']):
            return {
                "status_code": 401,
                "message": "You are not in the same team"
            }, 401
    else:
        # check if the token is valid.
        the_doc = TOKEN.find_one({
            "utorid": myUtorid
        })

        if not the_doc:
            return {
                "status_code": 401,
                "message": "The UTORid is not associated with a token. Please sign up first."
            }, 401

        if authorization_header != the_doc['token']:
            return {
                "status_code": 401,
                "message": "Invalid token"
            }, 401
        
@app.before_request
def before_request():
    response = api_key_middleware()
    if response is not None:
        return response

# An API that creates a grade document.
# The request body should be a JSON object with the following fields:
# utorid: the utorid of the student
# course: the course code
# grade: the grade of the student
# The response body should be a JSON object with the following fields:
# status: a code
# message: "Grade created successfully" if the grade document is created successfully, "Error creating grade" otherwise
# id: the id of the grade document created
@app.route('/grade', methods=['POST'])
@limiter.limit('50/minute')
def create_grade():
    try:
        course = request.json['course'] if 'course' in request.json else None
        grade = request.json['grade'] if 'grade' in request.json else None

        def is_integer(s):
            try:
                int(s)
                return True
            except ValueError:
                return False
        
        


        # get utorid from token.
        authorization_header = request.headers.get("Authorization")
        the_doc = TOKEN.find_one({
            "token": authorization_header
        })
        utorid = the_doc['utorid']

        if not utorid or not course or not grade:
            return {
                "status_code": 400,
                "message": "utorid, course, and grade are required"
            }, 400
        
        # check if the grade is valid.
        print(grade)
        if not is_integer(grade) or int(grade) < 0 or int(grade) > 100:
            return {
                "status_code": 400,
                "message": "grade must be an integer between 0 and 100"
            }, 400

        
        grade = int(grade)

        print(grade)

        # check if the grade document already exists

        the_doc = GRADE.find_one({
            "utorid": utorid,
            "course": course
        })
        if the_doc:
            return {
                "status_code": 409,
                "message": "Grade already exists"
            }, 409
        grade_id = GRADE.insert_one({
            "utorid": utorid,
            "course": course,
            "grade": grade
        }).inserted_id
        return {
            "status_code": 200,
            "message": "Grade created successfully",
            "id": str(grade_id)
        }, 200
    except PyMongoError as e:
        print(e)
        return {
            "status_code": 500,
            "message": "Error creating grade"
        }, 500
    
# An API that returns a grade document, it's a get request with the following path: grade/course/utorid
# The response body should be a JSON object with the following fields:
# status: a code
# message: "Grade retrieved successfully" if the grade document is retrieved successfully, "Error retrieving grade" otherwise
# grade: the grade of the student
@app.route('/grade', methods=['GET'])
@limiter.limit('50/minute')
def get_grade():
    try:
        utorid = request.args.get('utorid') if 'utorid' in request.args else None
        course = request.args.get('course') if 'course' in request.args else None
        the_doc = GRADE.find_one({
            "utorid": utorid,
            "course": course
        })
        if not the_doc:
            return {
                "status_code": 404,
                "message": "Grade not found or at least one of your teammates doesn't have the grade for this course (double check with your teammates on what course names they used to log)"
            }, 404
        return {
            "status_code": 200,
            "message": "Grade retrieved successfully",
            "utorid": the_doc['utorid'],
            "grade": json.loads(json_util.dumps(the_doc))
        }, 200
    except PyMongoError as e:
        print(e)
        return {
            "status_code": 500,
            "message": "Error retrieving grade"
        }, 500
    except Exception as e:
        print("error")
        print(e)
        return {
            "status_code": 500,
            "message": "Error retrieving grade"
        }, 500
    

@app.route('/grades', methods=['GET'])
@limiter.limit('50/minute')
def get_grades():
    try:
        utorid = request.args.get('utorid') if 'utorid' in request.args else None
        the_doc = GRADE.find_one({
            "utorid": utorid
        })
        if not the_doc:
            return {
                "status_code": 404,
                "message": "No grade found"
            }, 404
        return {
            "status_code": 200,
            "message": "Grades retrieved successfully",
            "utorid": the_doc['utorid'],
            "grades": json.loads(json_util.dumps(the_doc))
        }, 200
    except PyMongoError as e:
        print(e)
        return {
            "status_code": 500,
            "message": "Error retrieving grade"
        }, 500
    except Exception as e:
        print("error")
        print(e)
        return {
            "status_code": 500,
            "message": "Error retrieving grade"
        }, 500

# An API that updates a grade document.
# The request body should be a JSON object with the following fields:
# utorid: the utorid of the student
# course: the course code
# grade: the grade of the student
# The response body should be a JSON object with the following fields:
# status: a code
# message: "Grade updated successfully" if the grade document is updated successfully, "Error updating grade" otherwise
@app.route('/grade', methods=['PUT'])
@limiter.limit('50/minute')
def update_grade():
    try:
        authorization_header = request.headers.get("Authorization")
        the_doc = TOKEN.find_one({
            "token": authorization_header
        })
        utorid = the_doc['utorid']
        course = request.json['course'] if 'course' in request.json else None
        grade = request.json['grade'] if 'grade' in request.json else None

        if not utorid or not course or not grade:
            return {
                "status_code": 400,
                "message": "utorid, course, and grade are required"
            }
        
        # check if the grade is valid.
        if not isinstance(grade, int) or grade < 0 or grade > 100:
            return {
                "status_code": 400,
                "message": "grade must be an integer between 0 and 100"
            }

        # check if the grade document already exists

        the_doc = GRADE.find_one({
            "utorid": utorid,
            "course": course
        })
        if not the_doc:
            return {
                "status_code": 404,
                "message": "The grade does not exist, please create it first using POST /grade"
            }
        
        GRADE.update_one({
            "_id": the_doc['_id']
        }, {
            "$set": {
                "grade": grade
            }
        })
        return {
            "status_code": 200,
            "message": "Grade updated successfully"
        }
    except PyMongoError as e:
        print(e)
        return {
            "status_code": 500,
            "message": "Error updating grade"
        }
    

# An API that deletes a grade document.
# The request body should be a JSON object with the following fields:
# utorid: the utorid of the student
# course: the course code
# The response body should be a JSON object with the following fields:
# status: a code
# message: "Grade deleted successfully" if the grade document is deleted successfully, "Error deleting grade" otherwise
@app.route('/grade', methods=['DELETE'])
@limiter.limit('50/minute')
def delete_grade():
    try:
        authorization_header = request.headers.get("Authorization")
        the_doc = TOKEN.find_one({
            "token": authorization_header
        })
        utorid = the_doc['utorid']
        course = request.json['course'] if 'course' in request.json else None

        if not utorid or not course:
            return {
                "status_code": 400,
                "message": "utorid, course are required"
            }

        # check if the grade document already exists

        the_doc = GRADE.find_one({
            "utorid": utorid,
            "course": course
        })
        if not the_doc:
            return {
                "status_code": 404,
                "message": "The grade does not exist, there's no need to delete it."
            }
        
        GRADE.delete_one({
            "_id": the_doc['_id']
        })
        return {
            "status_code": 200,
            "message": "Grade deleted successfully"
        }
    except PyMongoError as e:
        print(e)
        return {
            "status_code": 500,
            "message": "Error updating grade"
        }

import random
import string
@app.route('/signUp', methods=['GET'])
@limiter.limit('600/hour')
def signUp():
    # get parameters from request
    utorid = request.args.get('utorid') if 'utorid' in request.args else None

    # generate a random api token.
    # generate deployment api token.

    # first, see if this utorid is associated with a token.
    the_doc = TOKEN.find_one({
        "utorid": utorid
    })
    if the_doc:
        return {
            "status_code": 200,
            "message": "Someone took this username. If you are not the owner of this username or you forgot your token, please sign up with a different username and use the new token instead."
        }
    def generate_token(length=32):
        # Define the characters that can be used in the token
        characters = string.ascii_letters + string.digits

        # Generate a random token using the specified length
        token = ''.join(random.choice(characters) for _ in range(length))

        return token

    token = generate_token()

    # save to DB.
    TOKEN.insert_one({
        "utorid": utorid,
        "token": token
    })

    # return with token
    return {
        "status_code": 200,
        "message": "Token generated successfully",
        "token": token
    }

# form a team.
@app.route('/team', methods=['POST'])
@limiter.limit('50/minute')
def form_team():
    name = request.json['name'] if 'name' in request.json else None
    authorization_header = request.headers.get("Authorization")
    the_doc = TOKEN.find_one({
        "token": authorization_header
    })

    utorid = the_doc['utorid']

    if not name:
        return {
            "status_code": 400,
            "message": "Name are required"
        }, 400

    # check if the team already exists.
    the_doc = TEAM.find_one({
        "name": name
    })
    if the_doc:
        return {
            "status_code": 409,
            "message": "Team already exists"
        }, 409

    # check if the utorid is already in a team.
    the_doc = TEAM.find_one({
        "members": {"$in": [utorid]}
    })
    if the_doc:
        return {
            "status_code": 400,
            "message": "You are already in a team. You can't form a new team until you leave your current team."
        }, 400
    
    # create a team.
    result = TEAM.insert_one({
        "name": name, 
        "members": [utorid]
    })

    # Retrieve the inserted document using its _id
    new_team = TEAM.find_one({"_id": result.inserted_id})

    # make the_doc a json object with name and members.

    return {
        "status_code": 200,
        "message": f'Team {name} created successfully',
        "team": json.loads(json_util.dumps(new_team))
    }, 200


#join a team.
@app.route('/team', methods=['PUT'])
@limiter.limit('50/minute')
def join_team():
    name = request.json['name'] if 'name' in request.json else None
    authorization_header = request.headers.get("Authorization")
    the_doc = TOKEN.find_one({
        "token": authorization_header
    })

    utorid = the_doc['utorid']

    if not name:
        return {
            "status_code": 400,
            "message": "Name are required"
        }, 400

    # check if the team already exists.
    the_doc = TEAM.find_one({
        "name": name
    })
    if not the_doc:
        return {
            "status_code": 404,
            "message": "Team doesn't exist."
        }, 404

    # check if the utorid is already in a team.
    the_doc = TEAM.find_one({
        "members": {"$in": [utorid]}
    })
    if the_doc:
        return {
            "status_code": 400,
            "message": "You are already in a team"
        }, 400
    # make the_doc a json object with name and members.
    TEAM.update_one({
        "name": name
    }, {
        "$push": {
            "members": utorid
        }
    })

    return {
        "status_code": 200,
        "message": f'Joined team: {name} successfully'
    }, 200


# leave a team
@app.route('/leaveTeam', methods=['PUT'])
@limiter.limit('50/minute')
def leave_team():
    # load utorid.
    authorization_header = request.headers.get("Authorization")
    the_doc = TOKEN.find_one({
        "token": authorization_header
    })
    utorid = the_doc['utorid']


    # check if the utorid is already in a team.
    the_doc = TEAM.find_one({
        "members": {"$in": [utorid]}
    })

    if not the_doc:
        return {
            "status_code": 400,
            "message": "You are not in a team"
        }, 400

    # remove the utorid from the team. But if the team only has one member, delete the team.
    if len(the_doc['members']) == 1:
        TEAM.delete_one({
            "_id": the_doc['_id']
        })
    else:
        TEAM.update_one({
            "_id": the_doc['_id']
        }, {
            "$pull": {
                "members": utorid
            }
        })

    return {
        "status_code": 200,
        "message": "You have left the team"
    }, 200


# get all teams
@app.route('/teams', methods=['GET'])
@limiter.limit('50/minute')
def get_all_teams():
    # return all teams.
    teams = TEAM.find({})

    team_list = []

    for team in teams:
        team_list.append(team['name'])

    return {
        "status_code": 200,
        "message": "Teams retrieved successfully",
        "teams": team_list
    }, 200


# get my team members
@app.route('/team', methods=['GET'])
@limiter.limit('50/minute')
def get_my_team():
    # load utorid from params.
    authorization_header = request.headers.get("Authorization")
    the_doc = TOKEN.find_one({
        "token": authorization_header
    })
    utorid = the_doc['utorid']

    # check if the utorid is in a team.

    the_doc = TEAM.find_one({
        "members": {"$in": [utorid]}
    })

    if not the_doc:
        return {
            "status_code": 404,
            "message": "You are not in a team"
        }, 404

    # return the team members.
    return {
        "status_code": 200,
        "message": "Team retrieved successfully",
        "team": json.loads(json_util.dumps(the_doc))
    }, 200


# get all data
@app.route('/getAlldata', methods=['GET'])
@limiter.limit('50/minute')
def getAlldata():

    # get all grades.
    grades = GRADE.find({})
    teams = TEAM.find({})
    tokens = TOKEN.find({})
    # return the team members.
    return {
        "status_code": 200,
        "grades": json.loads(json_util.dumps(grades)),
        "teams": json.loads(json_util.dumps(teams)),
        "tokens": json.loads(json_util.dumps(tokens))
    }, 200



if __name__ == '__main__':
    app.run(host="0.0.0.0", port=20112, debug=True, threaded=True)
