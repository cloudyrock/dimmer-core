package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import com.github.cloudyrock.dimmer.exception.DimmerConfigException;
import com.github.cloudyrock.dimmer.metadata.*;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

class FeatureBroker {


    private static final DimmerLogger logger = new DimmerLogger(FeatureBroker.class);

    private final FeatureObservable featureObservable;
    private final Set<FeatureMetadata> featureActions;
    private final Class<? extends RuntimeException> defaultException;

    private Consumer<Map<BehaviourKey, Function<FeatureInvocation, ?>>> subscriber;

    FeatureBroker(FeatureObservable featureObservable,
                  Set<FeatureMetadata> featureActions,
                  Class<? extends RuntimeException> defaultException) {
        this.featureActions = featureActions;
        this.featureObservable = featureObservable;
        this.defaultException = defaultException;
    }

    void start() {
        featureObservable.subscribe(this::process);
    }

    void setSubscriber(Consumer<Map<BehaviourKey, Function<FeatureInvocation, ?>>> subscriber) {
        this.subscriber = subscriber;
    }


    void process(FeatureUpdateEvent featureUpdateEvent) {
        if (featureActions != null && subscriber != null) {
            final Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours =
            featureActions.stream()
                    .filter(fm-> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                    .peek(fm -> logger.info("APPLIED feature [{}]", fm.toString()))
                    .collect(Collectors.toMap(FeatureMetadata::getKey, FeatureMetadata::getFunction));
            subscriber.accept(behaviours);
        }

    }
}
