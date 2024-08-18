package com.spqrta.state.common.util.optics

interface OpticStrict<S : Any, Sub : Any> : OpticGetStrict<S, Sub>, OpticSet<S, Sub>
