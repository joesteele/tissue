# Tissue

Tissue was made to ease the pain in using a single GitHub Issues repository to manage issues across multiple projects.

At Treehouse we use this single Issues repository approach. It makes it easier to enter new issues or find existing ones
without needing to know about or even have access to the various different repositories for our projects.

We use labels to scope issues to their projects, so naturally we have a lot of labels. The main idea behind Tissue is
that it allows you to scope your issues on a per-project basis. Just throw a `.tissue` file in the root of your project
with the comma-separated labels to scope on, and you'll get a pretty printed list of just those issues whenever you type
the `tissue` command.

#### Example
![tissue](http://d.pr/i/1kOBV+)

### Options and Commands

Available options are:

- `-l` or `--label` - comma-separated labels (use quotes for multi-word labels)
- `-m` or `--milestone` - scoped to a milestone
- `-u` or `--unscoped` - ignore the `.tissue` file
- `-a` or `--all` - show both open and closed issues (defaults to open)
- `-c` or `--closed` - show closed issues (defaults to open)
- `--mine` - show issues assigned to you
- `--limit` - limit results returned

Available commands are:

- `i <number>` or `issue <number>` - show an issue
- `o <number>` or `open <number>` - open an issue in your browser
- `new` or `n` - opens the new issue page in your browser


### Installing

The app uses Java 8. Run `./gradlew installApp` to build a binary located in `build/install/tissue/bin/tissue` of the
project dir. Move the binary to your load path and/or alias it.

The app depends on a GitHub token set for the ENV variable `TISSUE_TOKEN`. You can create a token on your
[settings page](https://github.com/settings/application). You'll also need to similarly set the repository to use (e.g.
`TISSUE_REPOSITORY=treehouse/issues`).

This was just a toy project to suit my own needs. If you find this useful and want me to add something feel free to
submit a pull request or fork it to suit your own.
