# Nbb and playwright

<img src="https://user-images.githubusercontent.com/284934/222552490-439cb704-d0b0-4650-b0fc-0e18f49423eb.png">

## How to run

In this directory, run `npm install` and then `npm run prepare`.

Then, run the test with `npx nbb -m example`

To run it without a browser opening (headless), run `CI=1 npx nbb -m example` for bash. In other shells make sure you can set `CI` for headless mode.

### deno

If you want to use deno, run `deno run -A jsr:@babashka/nbb -m example`. However, you'll have to prefix the playwright import. Change `["playwright$default" :refer [chromium]]` to `["npm:playwright$default" :refer [chromium]]`
