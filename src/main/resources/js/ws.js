import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'

let stompClient = null
let responseHandler = null

export function connect() {
    const socket = new SockJS('/sockjs')
    stompClient = Stomp.over(socket)
    stompClient.debug = () => {}
    stompClient.connect({} , frame => {
        stompClient.subscribe('/topic/responses', message => {
            if (responseHandler !== null) {
                responseHandler(JSON.parse(message.body))
            }
        })
    })
}

export function setHandler(handler) {
    responseHandler = handler
}

export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
}