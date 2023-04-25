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
    let
      system = "x86_64-darwin";
      pkgs = nixpkgs.legacyPackages.${system};
      cljpkgs = clj-nix.packages."${system}";
    in

      {
        packages.x86_64-darwin.default = cljpkgs.mkCljBin {
          projectSrc = ./.;
          name = "trace-a-ray/ray";
          main-ns = "trace-a-ray.core";
          jdkRunner = pkgs.jdk17_headless;
        };
      };
}
