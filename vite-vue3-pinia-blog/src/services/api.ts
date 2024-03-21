import { request, post } from '../utils/network/axios2'
import { ResponseResult,TokenType } from "/@/typings";



/**
http://localhost:9000/auth/refresh

{
    "code": 200000,
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQ29udGV4dCI6IntcImlkXCI6XCIxXCIsXCJpc1N1cGVyXCI6ZmFsc2UsXCJsb25nVGVybVwiOmZhbHNlLFwibmlja05hbWVcIjpcImxmalwiLFwicm9sZVwiOjEsXCJ1c2VybmFtZVwiOlwibGZqXCJ9Iiwic3ViIjoibGZqIiwiZXhwIjoxNzEwODYwMzUxfQ.DuOXtoIYOAuJX_Y9HByU4T8Z5wYn4BVL0-Nw6bkE_Tk",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQ29udGV4dCI6IntcImlkXCI6XCIxXCIsXCJpc1N1cGVyXCI6ZmFsc2UsXCJsb25nVGVybVwiOmZhbHNlLFwibmlja05hbWVcIjpcImxmalwiLFwicm9sZVwiOjEsXCJ1c2VybmFtZVwiOlwibGZqXCJ9Iiwic3ViIjoibGZqIiwiZXhwIjoxNzEwODYwMzYyfQ.Vyxrr7yDeEmDr7kQlPmFYGvYQCuq_41QYFfIfOZwlL0"
    },
    "message": "响应成功"
}
*/


