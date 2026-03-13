# Agent instructions

Guidance for AI and other automated tools working in this repo. Read this file when contributing code or performing repo-wide tasks.

## Your preferences

Add your preferences below. For example:

- **Commits:** How you want commit messages formatted (e.g. style, what to include, length).

If I specify a :"blurb" or "purpose" followed by a phrase, I mean that phrase to be the first line in the commit message. Otherwise, feel free to generate your own first line.

The second part of the commit, generate your own bried description that you normally would, including important implementation details. Try to limit it to a handful of lines (5 or so; use your discretion).

If I specify "with details", then after the first and second parts (above), I want you to paste in the summary (or summaries) you gave me for the actions you took on my behalf that are part of this commit.

- **Code style:** Conventions to follow.

- **Other:** When changing code in an area listed below, read the "Known regressions and fixes" section so the same issues are not reintroduced.

## Known regressions and fixes

Document regressions here (what broke, where, and how it was fixed) so agents can avoid reintroducing them.

- **Lightbox / mobile scroll:** On mobile, when scrolled down, the lightbox can appear off the top of the screen. **Cause:** Lightbox was rendered inside the scrollable container, so `position: fixed` was relative to the wrong containing block. **Fix:** Render the lightbox via `createPortal(..., document.body)` when `isMobile` so it is a direct child of `body` and stays viewport-fixed.

- **Spurious search results:** Sometimes results from a different search (or stale results) are shown; refreshing fixes it. **Cause:** Multiple search requests can be in flight when the user changes query, filters, troves, sort, or page quickly. Whichever response returns last was applied, so an older response could overwrite a newer one. On desktop, the in-flight request was not aborted when serving from cache or when a new search started from another code path. On mobile, search fetch did not use `AbortController` at all. **Fix:** (1) Abort any in-flight search at the *start* of `fetchSearch` (before cache check and before starting a new request), so cache hits and quick successive calls cancel the previous request. (2) In MobileApp, add `AbortController` and pass `signal` to `fetch` so in-flight requests are aborted when a new search runs. (3) Use a `searchRequestIdRef` in both apps: only apply `setSearchResult` (and related state) when the response’s request id still matches the current id, and only clear loading in `finally` for that request, so stale responses never overwrite the latest and loading state stays correct.

---

*(Write your preferences above this line.)*
