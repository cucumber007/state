# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.0.12]

### Added

- Show Squeeze mode tasks that are inactive because of squeezing
- Show last completed date in Squeeze mode tasks
- Setting for disabling periodic Dynalist updates

### Fixed

- Loading loop on Dynalist API error

## [0.0.11]

### Fixed

- Wrong estimate
- Crash in some circumstances

## [0.0.10]

### Fixed

- Long click on the task that is done doing nothing

## [0.0.9]

### Fixed

- Frozen time

## [0.0.8]

### Fixed

- Reset Day not resetting done state

## [0.0.7]

### Added

- `Squeeze` Flipper schedule that makes the task inactive if there is no time left for it until the
  rest
  of the day

## [0.0.6]

### Added

- Add parsing Routine node type from Dynalist
- Automatic resetting of the daily routines on date change
