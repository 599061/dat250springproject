<script>
    import CreateUser from './lib/CreateUser.svelte';
    import CreatePoll from './lib/CreatePoll.svelte';
    import PollVote   from './lib/PollVote.svelte';
    import { listUsers, listPolls } from './lib/api.js';

    let users      = $state([]);
    let polls      = $state([]);
    let selectedId = $state(null);

    async function refreshUsers() { users = await listUsers(); }
    async function refreshPolls() {
        polls = await listPolls();
        if (selectedId && !polls.some(p => p.id === selectedId)) selectedId = null;
    }

    $effect.root(() => {
        refreshUsers();
        refreshPolls();
    });
</script>

<main>
    <h1>PollApp</h1>

    <CreateUser onCreated={refreshUsers} />
    <CreatePoll {users} onCreated={({ id }) => { selectedId = id; refreshPolls(); }} />

    <section class="list">
        <h2>Polls</h2>
        {#if polls.length === 0}
            <p>No polls yet.</p>
        {:else}
            <ul>
                {#each polls as p (p.id)}
                    <li>
                        <button class:selected={selectedId === p.id} onclick={() => selectedId = p.id}>
                            #{p.id} — {p.question}
                        </button>
                    </li>
                {/each}
            </ul>
        {/if}
        <button onclick={refreshPolls}>↻ Refresh</button>
    </section>

    {#if selectedId}
        {#key selectedId}
            <PollVote pollId={selectedId} />
        {/key}
    {/if}
</main>

<style>
    main { font-family: system-ui, sans-serif; padding: 1rem; display: grid; gap: 1rem; }
    .list ul { list-style: none; padding: 0; }
    .list li { margin: .25rem 0; }
    .list button { padding: .4rem .7rem; border-radius: 8px; }
    .list button.selected { background: #eef; }
</style>
