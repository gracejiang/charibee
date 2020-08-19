# charibee

Charibee is a social Yelp for charities that allows users to find local organizations and nonprofits to volunteer at.

View the [video demo](https://youtu.be/tBCh-Qbd0PA) here!

### Application Features

**User Roles**
- [x] A user can create a new profile as either an organizer or a volunteer
  - [x] Organizers can create new organizations for volunteers to join and can edit their current organizations
  - [x] Volunteers can view all organizations registered and express interest in joining/leaving organizations
  - [x] Volunteers can add new interests and update current interests in their profile tab
  - [x] All users can edit their profiles including their username, bio, and account password
  - [x] All users can set a profile picture by taking a picture through the Android camera or by uploading a picture from their gallery
 
| organizers                                                   | both                                                         | volunteers                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| - can register new organizations<br>- can edit their current organizations | - can edit their profile<br>- can update their settings<br>- can look up users in community and view their profiles<br>- can message other users<br>- can view details of their current organizations | - can discover new organizations to join, with sorting/filtering options<br>- can join and leave organizations<br>- can add and update interests to their profile |
  
**Home Dashboard**
- [x] Organizers have the option to register new organizations
  - [x] Organizers can look up & set an organization's address in a separate Map Activity using the Google Maps SDK
- [x] Organizers can view all organizations that they've registered and can click into an organization to edit it
- [X] Volunteers can view all the organizations that they've joined and can click into an organization to leave it
- [x] All users can click into an organization to view more details about that organization
- [x] UI distinguishment between organizers and volunteers

**Discovery Page**
- [x] All organizations are displayed in a discovery page
- [x] Volunteers can search for specific organizations by their names
- [x] Volunteers can filter organizations by their categories
- [x] Volunteers can  long click on an organization to join it

**Messages Page**
- [x] Users can view a list of all chats sent and received with other users
- [x] Users can click into a chat to see all messages within that chat and to send a new message

**Community Page**
- [x] Users can look up other users by either their username or their full name
- [x] Users can click into another user's profile and view their profile picture, bio, and interests
- [x] Users can message other users

**Other FBU Application Requirements**
- [x] Icon animation on the welcome screen
- [x] The app uses the Material UI library for a clean UI experience
- [x] The app interacts with a Parse Database to store and retrieve information about users and organizations
