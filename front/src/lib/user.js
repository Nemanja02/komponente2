import { writable,get } from 'svelte/store';
import { browser } from '$app/environment';

// Define the initial state of the user store
const initialUser = {
    token: '',
    id: '',
    role: '',
    isLoggedIn: false,
};

// Create a writable store with the initial state
let localUser = initialUser;
if (browser) {
    localUser = JSON.parse(localStorage.getItem('user'));
}
export const user = writable(localUser);
