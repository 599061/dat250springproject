// const BASE = 'http://localhost:8080';

async function jsonFetch(url, options = {}) {
    const res = await fetch(url, {
        headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
        ...options
    });
    if (!res.ok) throw new Error(`${res.status} ${res.statusText}: ${await res.text()}`);
    return res.headers.get('content-type')?.includes('application/json') ? res.json() : null;
}

// Users
export const listUsers = () => jsonFetch(`/api/users`);
export const createUser = (username, email) =>
    jsonFetch(`/api/users`, { method: 'POST', body: JSON.stringify({ username, email }) });

// Polls
export const listPolls = () => jsonFetch(`/api/polls`);
export const getPoll   = (id) => jsonFetch(`/api/polls/${id}`);
export const createPoll = (payload) =>
    jsonFetch(`/api/polls`, { method: 'POST', body: JSON.stringify(payload) });
export const deletePoll = (id) =>
    jsonFetch(`/api/polls/${id}`, { method: 'DELETE' });

// Votes / results
export const castVote = (pollId, username, optionOrder) =>
    jsonFetch(`/api/polls/${pollId}/votes`, {
        method: 'POST',
        body: JSON.stringify({ username, optionOrder })
    });
export const getUsersVote = (pollId, username) =>
    jsonFetch(`/api/polls/${pollId}/votes?username=${encodeURIComponent(username)}`);
export const getResults = (pollId) => jsonFetch(`/api/polls/${pollId}/results`);
