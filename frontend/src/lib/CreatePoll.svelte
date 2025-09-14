<script>
    import { createPoll } from './api.js';

    let { users = [], onCreated = () => {} } = $props();

    let question        = $state('');
    let closesAtLocal   = $state('');
    let creatorUsername = $state('');
    let options         = $state([
        { order: 1, caption: 'Option A' },
        { order: 2, caption: 'Option B' }
    ]);
    let saving = $state(false);
    let error  = $state('');

    $effect(() => {
        if (!creatorUsername && users.length) {
            creatorUsername = users[0].username;
        }
    });

    function toIsoZ(local) {
        const d = local ? new Date(local) : new Date(Date.now() + 3600_000);
        const pad = (n) => String(n).padStart(2, '0');
        return `${d.getUTCFullYear()}-${pad(d.getUTCMonth()+1)}-${pad(d.getUTCDate())}` +
            `T${pad(d.getUTCHours())}:${pad(d.getUTCMinutes())}:${pad(d.getUTCSeconds())}Z`;
    }

    const ready = $derived(
        !!creatorUsername &&
        question.trim().length > 0 &&
        !!closesAtLocal &&
        options.filter(o => (o.caption || '').trim()).length >= 2
    );

    async function submit() {
        if (!ready || saving) return;
        saving = true; error = '';
        try {
            const payload = {
                question: question.trim(),
                closesAt: toIsoZ(closesAtLocal),
                creatorUsername,
                options: options
                    .map(o => ({ order: Number(o.order), caption: (o.caption || '').trim() }))
                    .filter(o => o.caption)
            };
            const view = await createPoll(payload);
            onCreated({ id: view.id, poll: view });
            question = ''; closesAtLocal = '';
            options = [{ order: 1, caption: '' }, { order: 2, caption: '' }];
        } catch (e) {
            error = e.message;
        } finally {
            saving = false;
        }
    }

    function addOption() {
        const next = options.length ? Math.max(...options.map(o => +o.order)) + 1 : 1;
        options = [...options, { order: next, caption: '' }];
    }
    function removeOption(order) {
        options = options.filter(o => o.order !== order);
    }
</script>

<div class="card">
    <h2>Create a poll</h2>

    <label>Creator
        <select bind:value={creatorUsername} disabled={users.length === 0}>
            {#if users.length === 0}
                <option value="">-- no users --</option>
            {:else}
                {#each users as u}
                    <option value={u.username}>{u.username}</option>
                {/each}
            {/if}
        </select>
    </label>

    <label>Question
        <input placeholder="What's your favorite color?" bind:value={question} />
    </label>

    <label>Closes at
        <input type="datetime-local" bind:value={closesAtLocal} />
    </label>

    <div class="options">
        <h3>Options</h3>
        {#each options as o (o.order)}
            <div class="row">
                <input class="order" type="number" min="1" bind:value={o.order} />
                <input class="caption" placeholder="Caption" bind:value={o.caption} />
                <button type="button" onclick={() => removeOption(o.order)} disabled={options.length<=2}>✕</button>
            </div>
        {/each}
        <button type="button" onclick={addOption}>+ Add option</button>
    </div>

    {#if error}<p class="error">{error}</p>{/if}

    <button onclick={submit} disabled={!ready || saving}>
        {saving ? 'Saving…' : 'Create poll'}
    </button>
</div>

<style>
    .card { padding:1rem; border:1px solid #ddd; border-radius:12px; max-width:640px; background:#fff; }
    label { display:block; margin:.5rem 0; }
    input, select, button { padding:.5rem; }
    .row { display:grid; grid-template-columns:80px 1fr 48px; gap:.5rem; margin:.25rem 0; }
    .error { color:#c00; margin-top:.5rem; }
</style>
