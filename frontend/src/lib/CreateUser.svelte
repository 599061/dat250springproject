<script>
    import { createUser } from './api.js';

    let { onCreated = () => {} } = $props();

    let username = $state('');
    let email    = $state('');
    let saving   = $state(false);
    let error    = $state('');

    const isEmail = (s) => /\S+@\S+\.\S+/.test(s);
    const ready   = $derived(username.trim().length > 0 && isEmail(email));

    async function submit() {
        if (!ready || saving) return;
        saving = true; error = '';
        try {
            const user = await createUser(username.trim(), email.trim());
            onCreated({ user });
            username = ''; email = '';
        } catch (e) {
            error = e.message;
        } finally {
            saving = false;
        }
    }
</script>

<form class="card" onsubmit= {submit}>
    <h2>Create a user</h2>
    <label>Username
        <input placeholder="your username" bind:value={username} autocomplete="username" />
    </label>
    <label>Email
        <input type="email" placeholder="your email" bind:value={email} autocomplete="email" />
    </label>
    {#if error}<p class="error">{error}</p>{/if}
    <button type="submit" disabled={!ready || saving}>
        {saving ? 'Saving…' : 'Create User'}
    </button>
</form>

<style>
    .card { padding: 1rem; border: 1px solid #ddd; border-radius: 12px; max-width: 640px; background: #fff; }
    label { display:block; margin:.5rem 0; }
    input { padding:.5rem; width:100%; }
    .error { color:#c00; margin-top:.5rem; }
</style>
