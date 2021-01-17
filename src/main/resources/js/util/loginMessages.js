export function successMessage(text) {
    return {
        type: 'SUCCESS',
        text
    }
}

export function infoMessage(text) {
    return {
        type: 'INFO',
        text
    }
}

export function failureMessage(text) {
    return {
        type: 'FAILURE',
        text
    }
}