# trace-a-ray

This repository captures my work while learning to implement a fully
tested ray-tracer. I'm attempting to do this in Clojure so that I can
learn the language. I am following along with the principles set forth
in [The Ray Tracer Challenge by Jamis
Buck](https://pragprog.com/book/jbtracer/the-ray-tracer-challenge).

## Installation

Clone the repository.

## Usage

Execute the following with nix installed and flakes enabled,

    $ nix run

## Options

Currently there are no options.

## Development

The development environment builds with Nix and notably
[clj-nix](https://github.com/jlesquembre/clj-nix). I used a pinned
version of clj-nix below so that the lockfile will get generated
consistently. I found the hash using `nix flake info github:jlesquembre/clj-nix`

### Updating Maven dependencies

    $ nix run github:jlesquembre/clj-nix/7d9e244ea96988524ba3bd6c2bbafdf0a5340b96#deps-lock
    $ nix build

## Examples

### Sphere projection
![2d projection](/doc/images/sphere.png)

The 2d projection above is a demonstration that a sphere in 3d
worldspace and be projected onto a plane.

### Performance

Performance is an ongoing consideration. Step one is to make the
implementations correct. Step two is to find optimal ways of
generating the scenes.

### Generating examples

1. Install netpbm
2. Execute the following
   `$ pnmtopng <file>.ppm > <file>.png`

## License

Copyright © 2019 Eric Stolten

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
