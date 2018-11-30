# Another logic circuit simulator

Very much based on [logic.ly](https://logic.ly).

## Mouse things:

- left - select/deselect things
- middle - zoom, move around
- right - click buttons & switches

## Keyboard things:

- ctrl+C, ctrl+V - does what you think (yes, the I/O format is just fancy text); Currently the only way to save things (warning: you'll also want to save `l` too if you've got ICs)
- p - clears the update queue
- q/e - rotate a thing
- i - create an integrated circuit from the selection (the input/output fields are space-separated lists in top-to-bottom drawing order). The names of the pins are the settable names clicking the I/O objects one-by-one
- l - copy all the ICs to clipboard
- delete - deletes selected things
- ctrl+I - import integrated circuits