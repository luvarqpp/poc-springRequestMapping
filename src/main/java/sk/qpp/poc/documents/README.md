# Entities definitions

## Read only fields
To make field read-only, we can use jackson serialization to  work only one way.
Have a look at **CarInfo.fwVersion** field.
It has `@JsonProperty(access = JsonProperty.Access.READ_ONLY)` annotation on it,
which cause that when reading data through rest api, given field is send to client,
but is ignored when client post data to server, thus effectively data sent by client does not hit jpa.

