<script>
    import { getPoll, castVote, getUsersVote, getResults, deletePoll } from './api.js';

    let { pollId } = $props();

    let voter       = $state('');
    let poll        = $state(null);
    let results     = $state([]);
    let currentVote = $state(null);
    let loading     = $state(false);
    let error       = $state('');

    async function loadAll(id) {
        loading = true; error = '';
        try {
            poll    = await getPoll(id);
            results = await getResults(id);
             currentVote = voter.trim() ? await getUsersVote(id, voter.trim()) : null;
        } catch (e) {
            error = e.message;
        } finally {
            loading = false;
        }
    }

    $effect(() => { if (pollId) loadAll(pollId); });

    async function refreshVote() {
        if (!pollId || !voter.trim()) { currentVote = null; return; }
        currentVote = await getUsersVote(pollId, voter.trim());
    }

    async function vote(order) {
        await castVote(pollId, voter, order);
        try { currentVote = await getUsersVote(pollId, voter); } catch { currentVote = null; }
        results = await getResults(pollId);
    }

    async function remove() {
        if (!confirm('Delete this poll?')) return;
        await deletePoll(pollId);
        poll = null;
    }

</script>

{#if !poll && !loading}
    <p>Poll not found or deleted.</p>
{:else if loading}
    <p>Loading poll #{pollId}…</p>
{:else}
    <div class="poll">
        <div class="head">
            <span class="badge">Poll #{pollId}</span>
            <div class="grow"></div>
            <button class="danger" onclick={remove}>Delete</button>
        </div>

        <h2>{poll.question}</h2>

        <div class="voter">
            <label>Voter username
                <input bind:value={voter} onchange={refreshVote} placeholder="username" />
            </label>
            {#if currentVote}
                <small>Current vote: option <b>{currentVote.optionOrder}</b></small>
            {/if}
        </div>

        <ul class="options">
            {#each poll.options as o (o.order)}
                <li class="opt">
                    <span>{o.caption}</span>
                    <button onclick={() => vote(o.order)}>
                        {currentVote && currentVote.optionOrder === o.order ? 'Change to this' : 'Vote'}
                    </button>
                    <span class="count">{(results.find(r => r.order === o.order)?.votes ?? 0)} votes</span>
                </li>
            {/each}
        </ul>

        {#if error}<p class="error">{error}</p>{/if}
    </div>
{/if}

<style>
    .poll { border:1px solid #ddd; border-radius:12px; padding:1rem; background:#fafafa; max-width:760px; }
    .head { display:flex; align-items:center; gap:1rem; }
    .grow { flex:1; }
    .badge { background:#e6f0ff; color:#1a56db; padding:.25rem .5rem; border-radius:8px; font-size:.85rem; }
    .danger { background:#c62828; color:white; border:0; padding:.5rem .75rem; border-radius:8px; }
    .options { list-style:none; margin:0; padding:0; }
    .opt { display:flex; align-items:center; gap:.75rem; padding:.5rem; border-bottom:1px solid #eee; }
    .count { margin-left:auto; color:#555; }
    .error { color:#c00; }
</style>
