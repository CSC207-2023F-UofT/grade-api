# Lab 3: Grade API program

## Task 0: Fork and clone this repo

1. As with the previous lab activities, start
by making a fork of this repo and cloning it.


## Task 1: Your API Token

**Please try to work through these steps ahead of your lab time on Monday. Your team and TA can
help you during the lab if you had any trouble, so don't worry if you don't get through Task 1 before lab.**

In order to use the Grade API, you will need
to use an API Token. To obtain your token, we are going to make a simple request
to the Grade API.

1. Go to https://hoppscotch.io. This is a tool like Postman, which can be used to quickly interact with APIs.
2. Beside the word GET, replace https://echo.hoppscotch.io/ with https://grade-logging-api.chenpan.ca/signUp.

Next, we need to specify the username which we want to sign up with. To do this, we add a parameter.

3. In the Parameters tab, choose `+ Add new` and set the parameter name to `utorid`.
4. For its value, choose whatever username you want to use. Make note of the name you choose, as you'll use it
during the lab. (Don't worry, you can repeat this process with a new username if you need to later!)
5. Press the `Send` button to make the request. You may receive an error that the request didn't go through. Scroll down and
select `proxy` as the middleware and press `Send` again. You should now see the result of the request.
6. If the username had been used before, you will see a response indicating that. Choose a new username and send the
request again. Copy your API token somewhere, as you will need that during the lab.

You can refer to `apiDocuments/signUp.md` in this repo for the documentation of this API request.

***

Alternative to the above: Enter `https://grade-logging-api.chenpan.ca/signUp?utorid=USERNAME` in any web browser
with `USERNAME` replaced with the username you want to use.

***

and log in with your teach.cs account.

Your API Token will be displayed. Keep the webpage open
or save the token somewhere as we will need it when using the Grade API.

This API Token is solely for your use, so we don't want to share
it with anyone else. In particular, you want to make sure not to accidentally
add it to your version control system. Each member of your team will have
their own personal API Token, so this information should not be shared. There are
different ways to achieve this, but we'll take one approach here which
is to use an environment variable when running the program.

2. Try running the main application. When you attempt to perform a request
like entering a grade, you'll get an error, since we have not yet set your
API Token. Stop the program and go to `Run -> Edit Configurations...`.

3. Open the Run Configuration for `Main` and find the `Environment Variables:`
field.
4. In that field, type `API_TOKEN=YOUR_TOKEN`, with YOUR_TOKEN replaced with your
actual API Token from step 1.
5. Click `Apply` and then `OK`.
6. Now, rerun the program and enter a grade for `207` in the `Log a Grade` menu. You should see a popup
telling you that your grade was successfully entered. You can then check your grade
by using the `Get a Grade` menu and specifying your utorid and `207` for the course.


## Task 2: Forming a team

As a team-building exercise, you will now work together to form a team using
this application. Team members in this program are able to view the grades of other
team members.

1. Choose a team name. Make it something unique to your team, as other teams will also
be picking team names and duplicate names aren't allowed.

2. Have one member of your team form a team with the name your team chose.

3. Each other member of the team should use the `Join a team` menu to request to join the team.

4. Current members of the team can then use the `Handle a new member request` menu to accept
new member requests.

5. Use the `See my team members` menu to confirm that you have all gotten onto your team.

6. Try looking up the grade another team member entered for `207` using the `Get a Grade` menu.

Now that you are all on the team, there is one small coding task for your
team to work through.


## Task 3: Coding a new feature

While this program has some useful core functionality which is provided by the Grade API,
there are certain things which the Grade API can't currently do.

1. As a team, brainstorm some potential additional features which this program could have.
Aim to come up with one feature per team member.

2. For each feature, think about whether it is possible to implement, given the current functionality
provided by the Grade API. If it isn't possible, identify what new capabilities would need to be added.
And if it is possible, think about what the computational steps would be to add the feature
to the program.

3. As a team, develop a plan for implementing a feature which you feel is possible to implement
by using the existing Grade API. See below for a suggested feature if your group isn't sure which one
to work on.

Suggested Feature:

Calculate the average grade of all team members in a given course. This can be achieved
using the existing Grade API.

For the feature, you will need to:

- add a new button for it (follow the template provided)
- add a use case class for it (follow the template provided)

Your team can work together or divide into subteams to tackle each necessary piece...

Note: One of you should invite the others
to one project so that you can push to a common repo.
Remember to work on a branch and use a pull request to contribute your code and review.