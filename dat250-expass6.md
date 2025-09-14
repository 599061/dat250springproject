# Software Technology Experiment 3: SPA

The goal of this assignment was to develop a frontend SPA
using svelte for the project from the previous experiment.

[Link to frontend](https://github.com/599061/dat250springproject/tree/main/frontend/src)
[Link to svelte-components](https://github.com/599061/dat250springproject/tree/main/frontend/src/lib)

### What was done
- Created *.svelte* components *CreatePoll*, *PollVote*, 
*CreateUser*, and edited *App.svelte* to work with these components.
- Edited *api.js* to fetch CRUD from backend.
- Changed to relative paths in *api.js* before using *npm run build*.

### Pending
- Quite a few GET 404 errors after building using *npm run build*, 
but it seems to work regardless.
- The application does not look aesthetically pleasing.
- After voting the voted option says "change vote to". Should say something else, or just greyed-out.
- Still no persistence. This will come later.