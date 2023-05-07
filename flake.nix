{
  description = "A clj-nix flake";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
    clj-nix = {
      url = "github:jlesquembre/clj-nix";
      inputs.flake-utils.follows = "flake-utils";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = { self, nixpkgs, flake-utils, clj-nix }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        cljpkgs = clj-nix.packages."${system}";
      in
        {
          packages = rec
            {
              trace-a-ray = cljpkgs.mkCljBin {
                projectSrc = ./.;
                name = "trace-a-ray/ray";
                main-ns = "trace-a-ray.core";
                jdkRunner = pkgs.jdk17_headless;

                doCheck = true;
                checkPhase = "clj -M:test";
              };
              default = trace-a-ray;
            };

          devShells = {
            trace-a-ray = pkgs.mkShell {
              name = "local-dev";
              buildInputs = [pkgs.bash pkgs.clojure pkgs.clojure-lsp];
            };
          };
        }
    );
}
