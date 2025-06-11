package com.spqrta.state.common.util.optics

interface Optic<S : Any, Sub : Any> : OpticOptional<S, Sub>, OpticGetStrict<S, Sub>
